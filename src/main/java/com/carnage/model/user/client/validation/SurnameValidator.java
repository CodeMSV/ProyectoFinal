package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class SurnameValidator {

    private final static String REGEX = "^\\p{L}+ \\p{L}+$";
    /**
     * Validates the given surname.
     *
     * @param surname the surname to validate
     * @throws InvalidTextEnterException if the surname is invalid
     */
    public static void validate(String surname) throws InvalidTextEnterException {
        if (surname == null || surname.trim().isEmpty()) {
            throw new InvalidTextEnterException("Surname cannot be empty");
        }
        String trimmed = surname.trim();
        if (!trimmed.matches(REGEX)) {
            throw new InvalidTextEnterException("Surname must contain only letters");
        }
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            throw new InvalidTextEnterException("Surname must be between 2 and 50 characters");
        }
    }
}
