package com.carnage.model.user.client.validation;

import com.carnage.util.dao.InvalidTextEnterException;

public class PhoneValidator {

    private static final String PHONE_REGEX = "^\\d{7,15}$";

    /**
     * Validates the given phone number.
     *
     * @param phone the phone number string to validate
     * @throws InvalidTextEnterException if the phone is invalid
     */
    public static void validate(String phone) throws InvalidTextEnterException {
        if (phone == null || phone.isEmpty()) {
            throw new InvalidTextEnterException("Phone number cannot be empty");
        }
        if (!phone.matches(PHONE_REGEX)) {
            throw new InvalidTextEnterException(
                    "Phone must be 7 to 15 digits, without spaces or symbols"
            );
        }
    }
}
