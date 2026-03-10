package com.micro.cart.exceptions;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String message,
        String path,
        LocalDateTime timeStamp
) {}
