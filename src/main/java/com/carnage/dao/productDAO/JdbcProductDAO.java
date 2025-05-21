package com.carnage.dao.productDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.Product;
import com.carnage.model.product.ProductCategory;
import com.carnage.util.DDBB.ConnectionDDBB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDAO implements ProductDAO {

    private final ConnectionDDBB cm;

    public JdbcProductDAO(ConnectionDDBB cm) {
        this.cm = cm;
    }

    @Override
    public void createProduct(Product product) throws DAOException {
        String sql = "INSERT INTO product (name, price, qty_in_stock, category, expiration_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantityInStock());
            ps.setString(4, product.getCategory().name());
            ps.setDate(5, Date.valueOf(product.getExpirationDate()));

            int affected = ps.executeUpdate();
            if (affected == 0) throw new DAOException("Creating product failed, no rows affected.");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) product.setId(rs.getInt(1));
                else throw new DAOException("Creating product failed, no ID obtained.");
            }

        } catch (SQLException ex) {
            throw new DAOException("Error creating product", ex);
        }
    }

    @Override
    public Product findById(int productId) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new EntityNotFoundException("Product not found with ID=" + productId);
                return mapRowToProduct(rs);
            }

        } catch (SQLException ex) {
            throw new DAOException("Error finding product by ID", ex);
        }
    }

    @Override
    public List<Product> findAll() throws DAOException {
        String sql = "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Product> list = new ArrayList<>();
            while (rs.next()) list.add(mapRowToProduct(rs));
            return list;

        } catch (SQLException ex) {
            throw new DAOException("Error fetching all products", ex);
        }
    }

    @Override
    public void updateProduct(Product product) throws DAOException, EntityNotFoundException {
        String sql = "UPDATE product SET name = ?, price = ?, qty_in_stock = ?, category = ?, expiration_date = ? WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getQuantityInStock());
            ps.setString(4, product.getCategory().name());
            ps.setDate(5, Date.valueOf(product.getExpirationDate()));
            ps.setInt(6, product.getId());

            int affected = ps.executeUpdate();
            if (affected == 0) throw new EntityNotFoundException("Product not found for update, ID=" + product.getId());

        } catch (SQLException ex) {
            throw new DAOException("Error updating product", ex);
        }
    }

    @Override
    public void deleteProduct(int productId) throws DAOException, EntityNotFoundException {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            int affected = ps.executeUpdate();
            if (affected == 0) throw new EntityNotFoundException("Product not found for deletion, ID=" + productId);

        } catch (SQLException ex) {
            throw new DAOException("Error deleting product", ex);
        }
    }

    @Override
    public List<Product> findByCategory(ProductCategory category) throws DAOException {
        String sql = "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product WHERE category = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) list.add(mapRowToProduct(rs));
                return list;
            }

        } catch (SQLException ex) {
            throw new DAOException("Error fetching products by category", ex);
        }
    }

    @Override
    public List<Product> findByNameContaining(String term) throws DAOException {
        String sql = "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product WHERE name LIKE ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + term + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) list.add(mapRowToProduct(rs));
                return list;
            }

        } catch (SQLException ex) {
            throw new DAOException("Error searching products by name", ex);
        }
    }

    @Override
    public List<Product> findLowStock(int threshold) throws DAOException {
        String sql = "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product WHERE qty_in_stock < ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) list.add(mapRowToProduct(rs));
                return list;
            }

        } catch (SQLException ex) {
            throw new DAOException("Error fetching low stock products", ex);
        }
    }

    /**
     * Mapea la fila actual a un objeto Product.
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product p = new Product(
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("qty_in_stock"),
                ProductCategory.valueOf(rs.getString("category")),
                rs.getDate("expiration_date").toLocalDate()
        );
        p.setId(rs.getInt("id"));
        return p;
    }
}
