package com.example.kyrsovay.ex;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String message;

    public RequestException(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
