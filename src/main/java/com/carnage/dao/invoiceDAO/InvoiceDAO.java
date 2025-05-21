package com.carnage.dao.invoiceDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.Invoice;

import java.util.List;

public interface InvoiceDAO {

    /**
     * Creates a new invoice in the database.
     *
     * @param invoice The invoice to create.
     * @throws DAOException If there is an error during the operation.
     */
    void createInvoice(Invoice invoice) throws DAOException;

    /**
     * Finds an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to find.
     * @return An Optional containing the found invoice, or an empty Optional if not found.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    Invoice findById(int invoiceId) throws DAOException, EntityNotFoundException;

    /**
     * Finds an invoice by its order ID.
     *
     * @param orderId The order ID of the invoice to find.
     * @return The found invoice.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    Invoice findByOrderId(int orderId) throws DAOException, EntityNotFoundException;

    /**
     * Finds all invoices in the database.
     *
     * @return A list of all invoices.
     * @throws DAOException If there is an error during the operation.
     */
    List<Invoice> findAll() throws DAOException;


    /**
     * Finds all invoices for a specific client.
     *
     * @param clientId The ID of the client.
     * @return A list of invoices for the specified client.
     * @throws DAOException If there is an error during the operation.
     */
    List<Invoice> findByClientId(int clientId) throws DAOException;


}
