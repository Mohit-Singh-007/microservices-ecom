package com.micro.user.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;
    private final String path;

    @Builder.Default
    private final Instant timestamp = Instant.now();

    // Only populated for validation errors (field-level)
    private final List<FieldError> fieldErrors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final Object rejectedValue;
        private final String reason;
    }
}