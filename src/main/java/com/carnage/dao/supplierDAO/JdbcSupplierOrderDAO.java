package com.carnage.dao.supplierDAO;

import com.carnage.model.sale.OrderStatus;
import com.carnage.model.supplier.SupplierOrder;
import com.carnage.model.supplier.SupplierOrderItem;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class JdbcSupplierOrderDAO implements SupplierOrderDAO {

    private final ConnectionDDBB databaseConnectionManager;
    private final JdbcSupplierOrderItemDAO itemDAO;


    public JdbcSupplierOrderDAO(ConnectionDDBB databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
        this.itemDAO = new JdbcSupplierOrderItemDAO(databaseConnectionManager);
    }


    /**
     * Creates a new supplier order in the database.
     *
     * @param order The supplier order to create.
     * @throws DAOException If an error occurs while creating the order.
     */
    @Override
    public void createOrder(SupplierOrder order) throws DAOException {
        String sql = "INSERT INTO supplier_order (order_date, status) VALUES (?, ?)";
        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1,
                    order.getCreatedAt() != null
                            ? Timestamp.valueOf(order.getCreatedAt())
                            : new Timestamp(System.currentTimeMillis())
            );
            ps.setString(2, order.getStatus().name());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating supplier order failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                } else {
                    throw new DAOException("Creating supplier order failed, no ID obtained.");
                }
            }

            for (SupplierOrderItem item : order.getItems()) {
                item.setId(order.getId());
                itemDAO.createItem(item);
            }

        } catch (SQLException e) {
            throw new DAOException("Error creating SupplierOrder", e);
        }
    }



    /**
     * Updates an existing supplier order in the database.
     *
     * @param order The supplier order to update.
     * @throws DAOException If an error occurs while updating the order.
     */
    @Override
    public List<SupplierOrder> findAll() throws DAOException {
        String sql = "SELECT id, order_date, status FROM supplier_order";
        List<SupplierOrder> orders = new ArrayList<>();
        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error fetching supplier orders", e);
        }
    }



    /**
     * Updates an existing supplier order in the database.
     *
     * @param order The supplier order to update.
     * @throws DAOException If an error occurs while updating the order.
     */
    @Override
    public SupplierOrder findById(int orderId) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, order_date, status FROM supplier_order WHERE id = ?";
        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("SupplierOrder not found with ID=" + orderId);
                }
                return mapRowToOrder(rs);
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching SupplierOrder by ID", e);
        }
    }



    /**
     * Updates an existing supplier order in the database.
     *
     * @param order The supplier order to update.
     * @throws DAOException If an error occurs while updating the order.
     */
    private SupplierOrder mapRowToOrder(ResultSet rs) throws SQLException, DAOException {
        int id   = rs.getInt("id");
        LocalDateTime date = rs.getTimestamp("order_date").toLocalDateTime();
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        List<SupplierOrderItem> items;
        try {
            items = itemDAO.findByOrderId(id);
        } catch (Exception ex) {
            items = new ArrayList<>();
        }

        SupplierOrder order = new SupplierOrder(items, status);
        order.setId(id);
        return order;
    }
}
