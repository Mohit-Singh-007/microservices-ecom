package com.micro.cart.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            ProductNotFoundException ex , HttpServletRequest req
    ){
        ApiError error = new ApiError(
                404,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(404).body(error);
    }


    @ExceptionHandler(HandleValidation.class)
    public ResponseEntity<ApiError> handleValidation(
            HandleValidation ex , HttpServletRequest req
    ){
        ApiError error = new ApiError(
                400,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(IllegalState.class)
    public ResponseEntity<ApiError> handleIllegalState(
            IllegalState ex , HttpServletRequest req
    ){
        ApiError error = new ApiError(
                400,
                ex.getMessage(),
                req.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex , HttpServletRequest req
    ){ApiError error = new ApiError(
            500,
            "Internal Server error",
            req.getRequestURI(),
            LocalDateTime.now()
    );
        return ResponseEntity.status(500).body(error);

    }


}
