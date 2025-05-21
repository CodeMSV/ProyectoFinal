package com.carnage.dao.supplierDAO;

import com.carnage.model.supplier.SupplierOrderItem;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSupplierOrderItemDAO implements SupplierOrderItemDAO {

    private final ConnectionDDBB cm;

    public JdbcSupplierOrderItemDAO(ConnectionDDBB cm) {
        this.cm = cm;
    }

    @Override
    public List<SupplierOrderItem> findByOrderId(int orderId) throws DAOException {
        String sql = "SELECT id, product_id, quantity FROM supplier_order_item WHERE order_id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<SupplierOrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    SupplierOrderItem item = new SupplierOrderItem(
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            orderId    // asociamos por ID, no por objeto
                    );
                    // asignamos el id generado en BD al objeto
                    item.setId(rs.getInt("id"));
                    items.add(item);
                }
                return items;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching SupplierOrderItems", e);
        }
    }

    @Override
    public void createItem(SupplierOrderItem item) throws DAOException {
        String sql = "INSERT INTO supplier_order_item (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // solo seteamos FK order_id, producto y cantidad
            ps.setInt(1, item.getSupplierOrder());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating SupplierOrderItem failed, no rows affected.");
            }

            // recuperar id generado
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
