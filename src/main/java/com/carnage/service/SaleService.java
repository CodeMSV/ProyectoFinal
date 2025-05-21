package com.carnage.service;

import com.carnage.dao.saleDAO.SaleDAO;
import com.carnage.util.dao.DAOException;
import com.carnage.model.Sale;

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


    public void createSale(Sale sale) throws DAOException {
        saleDAO.createSale(sale);
    }
}
