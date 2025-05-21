package com.carnage.dao.userDAO;

import com.carnage.util.dao.DAOException;

import com.carnage.model.user.client.PaymentMethod;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public interface PaymentMethodDAO {

    /**
     * Retrieves all available payment methods.
     *
     * @return a list of PaymentMethod
     * @throws DAOException if a data access error occurs
     */
    List<PaymentMethod> findAll() throws DAOException;

    /**
     * Retrieves a payment method by its identifier.
     *
     * @param id the payment-method identifier
     * @return the PaymentMethod
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if no payment method is found
     */
    PaymentMethod findById(int id) throws DAOException, EntityNotFoundException;
}
