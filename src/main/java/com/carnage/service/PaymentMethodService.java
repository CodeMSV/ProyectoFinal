package com.carnage.service;

import com.carnage.dao.userDAO.PaymentMethodDAO;
import com.carnage.util.dao.DAOException;
import com.carnage.model.user.client.PaymentMethod;

import java.util.List;

public class PaymentMethodService {

    private PaymentMethodDAO paymentMethodDAO;

    public PaymentMethodService(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }

    /**
     * Retrieves all available payment methods.
     *
     * @return list of PaymentMethod
     * @throws DAOException if a data access error occurs
     */
    public List<PaymentMethod> listAll() throws DAOException {
        return paymentMethodDAO.findAll();
    }
}
