package com.pm.authservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){

        log.warn("Email address already exists {}", ex.getMessage());

        Map<String, String> result = new HashMap<>();
        result.put("message", "Email address already exists");
        return ResponseEntity.badRequest().body(result);
    }
    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<Map<String, String>> handleJsonParsingException(EmailAlreadyExistsException ex){

        log.warn("A problem ocurred parsing json{}", ex.getMessage());

        Map<String, String> result = new HashMap<>();
        result.put("message", "A problem ocurred parsing json");
        return ResponseEntity.internalServerError().body(result);
    }
}
