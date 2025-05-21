package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class NameValidator {


    /**
     * Validates the given name.
     *
     * @param name the first name to validate
     * @throws InvalidTextEnterException if the name is invalid
     */
    public static void validate(String name) throws InvalidTextEnterException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidTextEnterException("Name cannot be empty");
        }
        String trimmed = name.trim();
        if (!trimmed.matches("^[\\p{L}]+$")) {
            throw new InvalidTextEnterException("Name must contain only letters");
        }
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            throw new InvalidTextEnterException("Name must be between 2 and 50 characters");
        }
    }
}