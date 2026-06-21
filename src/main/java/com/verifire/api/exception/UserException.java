package com.verifire.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {

    private final HttpStatus statusCode;

    public UserException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
