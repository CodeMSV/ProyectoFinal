package com.carnage.dao.supplierDAO;

import com.carnage.model.supplier.SupplierOrderItem;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSupplierOrderItemDAO implements SupplierOrderItemDAO {

    private final ConnectionDDBB databaseConnectionManager;

    public JdbcSupplierOrderItemDAO(ConnectionDDBB databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }


    /**
     * Retrieves a list of SupplierOrderItems associated with a specific order ID.
     *
     * @param orderId the ID of the supplier order
     * @return a list of SupplierOrderItems
     * @throws DAOException if an error occurs while accessing the database
     */
    @Override
    public List<SupplierOrderItem> findByOrderId(int orderId) throws DAOException {
        String sql = "SELECT id, product_id, quantity FROM supplier_order_item WHERE order_id = ?";
        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<SupplierOrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    SupplierOrderItem item = new SupplierOrderItem(
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            orderId
                    );

                    item.setId(rs.getInt("id"));
                    items.add(item);
                }
                return items;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching SupplierOrderItems", e);
        }
    }


    /**
     * Creates a new SupplierOrderItem in the database.
     *
     * @param item the SupplierOrderItem to create
     * @throws DAOException if an error occurs while accessing the database
     */
    @Override
    public void createItem(SupplierOrderItem item) throws DAOException {
        String sql = "INSERT INTO supplier_order_item (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            ps.setInt(1, item.getSupplierOrder());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating SupplierOrderItem failed, no rows affected.");
            }


            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                } else {
                    throw new DAOException("Creating SupplierOrderItem failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error creating SupplierOrderItem", e);
        }
    }
}
