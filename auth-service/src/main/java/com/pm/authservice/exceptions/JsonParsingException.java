package com.pm.authservice.exceptions;

public class JsonParsingException extends RuntimeException {
    public JsonParsingException(String message) {
        super(message);
    }
}
