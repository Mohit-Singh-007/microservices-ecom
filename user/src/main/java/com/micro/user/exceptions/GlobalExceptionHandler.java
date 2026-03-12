package com.micro.user.exceptions;


import com.micro.user.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ── Our custom base exceptions ────────────────────────────────────────────

    /**
     * Handles all exceptions that extend BaseException (typed, known errors).
     * Logs at WARN — these are expected business/integration failures.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex, HttpServletRequest request) {

        log.warn("[{}] {} — path: {}", ex.getErrorCode(), ex.getMessage(), request.getRequestURI());

        ErrorResponse body = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    /**
     * Special handling for the CRITICAL case — orphaned Keycloak user.
     * Logs at ERROR and ideally triggers an alert / dead-letter write.
     */
    @ExceptionHandler(KeycloakRollbackFailureException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRollbackFailure(
            KeycloakRollbackFailureException ex, HttpServletRequest request) {

        // ⚠️ CRITICAL — alert your on-call / ops team here
        // e.g. alertService.triggerOrphanAlert(ex.getOrphanedKeycloakId());
        log.error(
                "CRITICAL ORPHAN — Keycloak user '{}' requires manual cleanup. Path: {}",
                ex.getOrphanedKeycloakId(),
                request.getRequestURI(),
                ex
        );

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ex.getErrorCode())
                .message("Registration failed. Please try again or contact support.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // ── Spring validation (@Valid) ────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(e -> e instanceof FieldError)
                .map(e -> (FieldError) e)
                .map(fe -> ErrorResponse.FieldError.builder()
                        .field(fe.getField())
                        .rejectedValue(fe.getRejectedValue())
                        .reason(fe.getDefaultMessage())
                        .build())
                .toList();

        log.warn("Validation failed on {}: {} field error(s)", request.getRequestURI(), fieldErrors.size());

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("VALIDATION_FAILED")
                .message("Request validation failed")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ── Fallback — unexpected exceptions ─────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex, HttpServletRequest request) {

        // Log full stack trace — this should never happen in normal operation
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
