package com.carnage.dao.invoiceDAO;

import com.carnage.model.Invoice;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class JdbcInvoiceDAO implements InvoiceDAO {

    /**
     * Creates a new invoice in the database.
     *
     * @param invoice the invoice to create
     * @throws DAOException if an error occurs while creating the invoice
     */
    @Override
    public void createInvoice(Invoice invoice) throws DAOException {
        String sql = "INSERT INTO invoice (order_id, invoice_date, invoice_pdf) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDDBB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getSaleId());
            ps.setDate(2, Date.valueOf(invoice.getInvoiceDate()));
            ps.setBytes(3, invoice.getInvoicePdf());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating invoice failed, no rows affected.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    invoice.setId(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Error creating invoice", ex);
        }
    }


    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice the invoice to update
     * @throws DAOException if an error occurs while updating the invoice
     */
    @Override
    public Invoice findById(int invoiceId) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, order_id, invoice_date, invoice_pdf FROM invoice WHERE id = ?";
        try (Connection conn = ConnectionDDBB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Invoice not found with ID=" + invoiceId);
                }
                Invoice inv = mapRowToInvoice(rs);
                return inv;
            }
        } catch (SQLException ex) {
            throw new DAOException("Error finding invoice by ID", ex);
        }
    }


    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice the invoice to update
     * @throws DAOException if an error occurs while updating the invoice
     */
    @Override
    public Invoice findByOrderId(int orderId) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, order_id, invoice_date, invoice_pdf FROM invoice WHERE order_id = ?";
        try (Connection conn = ConnectionDDBB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Invoice not found for order ID=" + orderId);
                }
                return mapRowToInvoice(rs);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error finding invoice by order ID", ex);
        }
    }


    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice the invoice to update
     * @throws DAOException if an error occurs while updating the invoice
     */
    @Override
    public List<Invoice> findAll() throws DAOException {
        String sql = "SELECT id, order_id, invoice_date, invoice_pdf FROM invoice";
        try (Connection conn = ConnectionDDBB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Invoice> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRowToInvoice(rs));
            }
            return list;
        } catch (SQLException ex) {
            throw new DAOException("Error fetching all invoices", ex);
        }
    }


    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice the invoice to update
     * @throws DAOException if an error occurs while updating the invoice
     */
    @Override
    public List<Invoice> findByClientId(int clientId) throws DAOException {
        String sql =
                "SELECT i.id, i.order_id, i.invoice_date, i.invoice_pdf " +
                        "FROM invoice i " +
                        "JOIN sales s ON i.order_id = s.id " +
                        "WHERE s.client_id = ?";
        try (Connection conn = ConnectionDDBB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Invoice> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRowToInvoice(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw new DAOException("Error fetching invoices by client ID", ex);
        }
    }

    /**
     * Updates an existing invoice in the database.
     *
     * @param invoice the invoice to update
     * @throws DAOException if an error occurs while updating the invoice
     */
    private Invoice mapRowToInvoice(ResultSet resultSet) throws SQLException {
        int invoiceId = resultSet.getInt("id");
        int linkedOrderId = resultSet.getInt("order_id");
        LocalDate invoiceDate = resultSet.getDate("invoice_date").toLocalDate();
        byte[] invoicePdfBytes = resultSet.getBytes("invoice_pdf");

        Invoice invoice = new Invoice(linkedOrderId, invoiceDate, invoicePdfBytes);
        invoice.setId(invoiceId);
        return invoice;
    }

}
