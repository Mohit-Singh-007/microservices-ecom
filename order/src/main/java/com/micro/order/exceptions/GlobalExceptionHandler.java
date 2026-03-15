package com.micro.order.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            EmptyCartException ex, HttpServletRequest req
    ) {
        ApiError error = new ApiError(
                400,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            InsufficientStockException ex, HttpServletRequest req
    ) {
        ApiError error = new ApiError(
                400,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            OrderNotFoundException ex, HttpServletRequest req
    ) {
        ApiError error = new ApiError(
                404,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(Invalidstatustransitionexception.class)
    public ResponseEntity<ApiError> handleInvalidStatus(
            Invalidstatustransitionexception ex, HttpServletRequest req
    ) {
        ApiError error = new ApiError(
                400,
                "INVALID_STATUS_TRANSITION",
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(400).body(error);
    }


}
