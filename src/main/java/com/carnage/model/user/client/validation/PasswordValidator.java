package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class PasswordValidator {

    private static final String COMPLEXITY_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).+$";

    /**
     * Validates the given password.
     *
     * @param password the password to validate
     * @throws InvalidTextEnterException if the password is invalid
     */
    public static void validate(String password) throws InvalidTextEnterException {
        if (password == null || password.isEmpty()) {
            throw new InvalidTextEnterException("Password cannot be empty");
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new InvalidTextEnterException("Password must be between 6 and 20 characters");
        }
        if (!password.matches(COMPLEXITY_REGEX)) {
            throw new InvalidTextEnterException("Password must contain letters and digits");
        }
    }
}
