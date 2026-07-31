package com.developer.productservice.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path) {

    static ErrorResponse of(int status, String error, String path) {
        return new ErrorResponse(Instant.now(), status, error, path);
    }
}
