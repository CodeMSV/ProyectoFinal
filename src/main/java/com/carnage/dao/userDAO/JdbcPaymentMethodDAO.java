package com.carnage.dao.userDAO;

import com.carnage.model.user.client.PaymentMethod;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;


import java.util.Arrays;
import java.util.List;


public class JdbcPaymentMethodDAO implements PaymentMethodDAO {

    /**
     * Returns all available payment methods.
     *
     * @return list of PaymentMethod enum constants
     * @throws DAOException never thrown in this implementation
     */
    @Override
    public List<PaymentMethod> findAll() throws DAOException {
        return Arrays.asList(PaymentMethod.values());
    }

    /**
     * Returns the payment method corresponding to the given ordinal.
     *
     * @param id the ordinal index of the enum constant
     * @return the PaymentMethod enum constant
     * @throws DAOException            never thrown in this implementation
     * @throws EntityNotFoundException if the ordinal is out of range
     */
    @Override
    public PaymentMethod findById(int id) throws DAOException, EntityNotFoundException {
        PaymentMethod[] vals = PaymentMethod.values();
        if (id < 0 || id >= vals.length) {
            throw new EntityNotFoundException("PaymentMethod not found for id (ordinal): " + id);
        }
        return vals[id];
    }
}
