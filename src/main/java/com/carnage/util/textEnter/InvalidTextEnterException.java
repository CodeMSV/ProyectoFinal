package com.carnage.util.textEnter;

public class InvalidTextEnterException extends RuntimeException {

    public InvalidTextEnterException(String message) {
        super(message);
    }

    public InvalidTextEnterException errorCompositionTextEnter(String value) {
        return new InvalidTextEnterException(String.format("%s is not a valid text enter", value));
    }
}
