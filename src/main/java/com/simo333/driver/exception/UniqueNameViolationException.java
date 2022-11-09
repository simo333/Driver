package com.simo333.driver.exception;

public class UniqueNameViolationException extends RuntimeException {
    public UniqueNameViolationException(String message) {
        super(message);
    }
}
