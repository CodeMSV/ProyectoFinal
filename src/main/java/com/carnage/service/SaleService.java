// src/main/java/com/carnage/service/SaleService.java
package com.carnage.service;

import com.carnage.dao.saleDAO.SaleDAO;
import com.carnage.model.Sale;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public class SaleService {

    private SaleDAO saleDAO;

    public SaleService(SaleDAO saleDAO) {
        this.saleDAO = saleDAO;
    }

    /**
     * Retrieves all sales for the given client.
     *
     * @param clientId the client identifier
     * @return list of Sale
     * @throws DAOException if a data access error occurs
     */
    public List<Sale> getSalesByClient(int clientId) throws DAOException {
        return saleDAO.findByClientId(clientId);
    }

    /**
     * Creates a new sale record.
     *
     * @param sale the Sale to create
     * @throws DAOException if a data access error occurs
     */
    public void createSale(Sale sale) throws DAOException {
        saleDAO.createSale(sale);
    }

    /**
     * Retrieves a sale by its ID.
     *
     * @param saleId the sale identifier
     * @return the found Sale
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if no sale with that ID
     */
    public Sale getSaleById(int saleId) throws DAOException, EntityNotFoundException {
        return saleDAO.findById(saleId);
    }

    /**
     * Updates an existing sale record.
     *
     * @param sale the Sale to update
     * @throws DAOException if a data access error occurs
     */
    public int countSalesByClient(int clientId) throws DAOException {
        return saleDAO.findByClientId(clientId).size();
    }
}
