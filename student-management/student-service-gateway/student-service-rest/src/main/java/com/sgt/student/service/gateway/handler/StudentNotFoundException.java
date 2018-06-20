package com.sgt.student.service.gateway.handler;

import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException() {
        this(HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
