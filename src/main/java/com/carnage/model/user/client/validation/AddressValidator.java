package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class AddressValidator {

    /**
     * Validates the given address.
     *
     * @param address the address to validate
     * @throws InvalidTextEnterException if the address is invalid
     */
    public static void validate(String address) throws InvalidTextEnterException {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidTextEnterException("Address cannot be empty");
        }
        String trimmed = address.trim();
        if (trimmed.length() < 5 || trimmed.length() > 100) {
            throw new InvalidTextEnterException("Address must be between 5 and 100 characters");
        }
    }
}
