package com.carnage.model.user.client.validation;

import com.carnage.model.user.client.PaymentMethod;
import com.carnage.util.dao.InvalidTextEnterException;

public class PaymentMethodValidator {

    /**
     * Validates the given payment method.
     *
     * @param pm the payment method to validate
     * @throws InvalidTextEnterException if no method is selected
     */
    public static void validate(PaymentMethod pm) throws InvalidTextEnterException {
        if (pm == null) {
            throw new InvalidTextEnterException("A payment method must be selected");
        }
    }
}
