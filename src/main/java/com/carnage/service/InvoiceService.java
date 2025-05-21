package com.carnage.service;

import com.carnage.dao.invoiceDAO.InvoiceDAO;
import com.carnage.util.dao.DAOException;
import com.carnage.model.Invoice;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class InvoiceService {

    private InvoiceDAO invoiceDAO;

    /**
     * Constructs an InvoiceService.
     *
     * @param invoiceDAO DAO for invoices
     */
    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    /**
     * Retrieves all invoices for the given client.
     *
     * @param clientId the client identifier
     * @return list of Invoice
     * @throws DAOException if a data access error occurs
     */
    public List<Invoice> getInvoicesByClient(int clientId) throws DAOException {
        return invoiceDAO.findByClientId(clientId);
    }


    /**
     * Retrieves an invoice by its associated order ID.
     *
     * @param orderId the order identifier
     * @return Optional containing the Invoice if found, or empty if none
     * @throws DAOException if a data access error occurs
     */
    public Optional<Invoice> getInvoiceByOrderId(int orderId) throws DAOException {
        try {
            return Optional.of(invoiceDAO.findByOrderId(orderId));
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }
}
