package com.carnage.util.dao;

public class InvalidTextEnterException extends RuntimeException {
    public InvalidTextEnterException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a message and a cause.
     *
     * @param message description of the validation failure
     * @param cause   the underlying cause
     */
    public InvalidTextEnterException(String message, Throwable cause) {
        super(message, cause);
    }
}
