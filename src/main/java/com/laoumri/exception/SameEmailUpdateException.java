package com.laoumri.exception;

public class SameEmailUpdateException extends RuntimeException {
    public SameEmailUpdateException() {
    }

    public SameEmailUpdateException(String message) {
        super(message);
    }
}
