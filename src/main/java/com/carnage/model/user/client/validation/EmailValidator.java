package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class EmailValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


    /**
     * Validates the given email.
     *
     * @param email the email to validate
     * @throws InvalidTextEnterException if the email is invalid
     */
    public static void validate(String email) throws InvalidTextEnterException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidTextEnterException("Email cannot be empty");
        }
        String trimmed = email.trim();
        if (!trimmed.matches(EMAIL_REGEX)) {
            throw new InvalidTextEnterException("Email format is invalid");
        }
    }
}
