package com.carnage.dao.saleDAO;

import com.carnage.model.Product;
import com.carnage.model.Sale;
import com.carnage.model.user.client.PaymentMethod;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcSaleDAO implements SaleDAO {

    private final ConnectionDDBB cm;

    public JdbcSaleDAO(ConnectionDDBB cm) {
        this.cm = cm;
    }

    @Override
    public void createSale(Sale sale) throws DAOException {
        String sql = "INSERT INTO sale (client_id, sale_date, total_price, payment_method) VALUES (?, ?, ?, ?)";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, sale.getClientId());
            ps.setDate(2, Date.valueOf(sale.getDate()));
            ps.setDouble(3, sale.getTotalPrice());
            ps.setString(4, sale.getPaymentMethod().name());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating sale failed, no rows affected.");
            }

            // Recuperar el ID generado y asignarlo al objeto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    sale.setId(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Error creating sale", ex);
        }
    }

    @Override
    public Sale findById(int saleId) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, client_id, sale_date, total_price, payment_method FROM sale WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Sale not found with ID=" + saleId);
                }
                return mapRowToSale(rs);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error finding sale by ID", ex);
        }
    }

    @Override
    public List<Sale> findAll() throws DAOException {
        String sql = "SELECT id, client_id, sale_date, total_price, payment_method FROM sale";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Sale> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRowToSale(rs));
            }
            return list;
        } catch (SQLException ex) {
            throw new DAOException("Error fetching all sales", ex);
        }
    }

    @Override
    public List<Sale> findByClientId(int clientId) throws DAOException {
        String sql = "SELECT id, client_id, sale_date, total_price, payment_method FROM sale WHERE client_id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Sale> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRowToSale(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw new DAOException("Error fetching sales by client", ex);
        }
    }

    /**
     * Mapea la fila actual del ResultSet a un objeto Sale,
     * asignando también el id generado.
     */
    private Sale mapRowToSale(ResultSet rs) throws SQLException {
        int id        = rs.getInt("id");
        int clientId  = rs.getInt("client_id");
        LocalDate date = rs.getDate("sale_date").toLocalDate();
        double total  = rs.getDouble("total_price");
        PaymentMethod pm = PaymentMethod.valueOf(rs.getString("payment_method"));

        // Por ahora dejamos la lista de productos vacía; luego la podrás poblar via SaleItemDAO
        Sale sale = new Sale(clientId, new ArrayList<Product>(), total, date, pm);
        sale.setId(id);
        return sale;
    }
}
