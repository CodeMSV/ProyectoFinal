package com.carnage.dao.saleDAO;

import com.carnage.util.dao.DAOException;

import com.carnage.model.Sale;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public interface SaleDAO {

    /**
     * Creates a new sale in the database.
     *
     * @param sale The sale to be created.
     * @throws DAOException If there is an error during the operation.
     */
    void createSale(Sale sale) throws DAOException;

    /**
     * Finds a sale by its ID.
     *
     * @param saleId The ID of the sale to be found.
     * @return The found sale.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the sale is not found.
     */
    Sale findById(int saleId) throws DAOException, EntityNotFoundException;

    /**
     * Finds all sales in the database.
     *
     * @return A list of all sales.
     * @throws DAOException If there is an error during the operation.
     */
    List<Sale> findAll() throws DAOException;


    /**
     * Finds all sales associated with a specific client ID.
     *
     * @param clientId The ID of the client.
     * @return A list of sales associated with the client.
     * @throws DAOException If there is an error during the operation.
     */
    List<Sale> findByClientId(int clientId) throws DAOException;


}
