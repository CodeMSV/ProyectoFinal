package com.carnage.dao.productDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.Product;
import com.carnage.model.product.ProductCategory;
import com.carnage.util.DDBB.ConnectionDDBB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDAO implements ProductDAO {

    private final ConnectionDDBB databaseConnectionManager;

    public JdbcProductDAO(ConnectionDDBB databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }


    /**
     * Creates a new product in the database.
     *
     * @param product The product to create.
     * @throws DAOException If there is an error during the operation.
     */
    @Override
    public void createProduct(Product product) throws DAOException {
        String insertProductSql = "INSERT INTO product (name, price, qty_in_stock, category, expiration_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement insertStatement = dbConnection.prepareStatement(
                     insertProductSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, product.getName());
            insertStatement.setDouble(2, product.getPrice());
            insertStatement.setInt(3, product.getQuantityInStock());
            insertStatement.setString(4, product.getCategory().name());
            insertStatement.setDate(5, Date.valueOf(product.getExpirationDate()));

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating product failed, no ID obtained.");
                }
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error creating product", sqlException);
        }
    }


    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product to find.
     * @return An Optional containing the found product, or an empty Optional if not found.
     * @throws DAOException            If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    @Override
    public Product findById(int productId) throws DAOException, EntityNotFoundException {
        String selectProductByIdSql =
                "SELECT id, name, price, qty_in_stock, category, expiration_date "
                        + "FROM product WHERE id = ?";

        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectProductByIdSql)) {

            selectStatement.setInt(1, productId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntityNotFoundException(
                            "Product not found with ID=" + productId
                    );
                }
                return mapRowToProduct(resultSet);
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error finding product by ID", sqlException);
        }
    }


    /**
     * Finds all products in the database.
     *
     * @return A list of all products.
     * @throws DAOException If there is an error during the operation.
     */
    @Override
    public List<Product> findAll() throws DAOException {
        String selectAllProductsSql =
                "SELECT id, name, price, qty_in_stock, category, expiration_date FROM product";

        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectAllProductsSql);
             ResultSet resultSet = selectStatement.executeQuery()) {

            List<Product> productsList = new ArrayList<>();
            while (resultSet.next()) {
                productsList.add(mapRowToProduct(resultSet));
            }
            return productsList;

        } catch (SQLException sqlException) {
            throw new DAOException("Error fetching all products", sqlException);
        }
    }


    /**
     * Updates an existing product in the database.
     *
     * @param product The product to update.
     * @throws DAOException            If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    @Override
    public void updateProduct(Product product) throws DAOException, EntityNotFoundException {
        String updateProductSql =
                "UPDATE product "
                        + "SET name = ?, price = ?, qty_in_stock = ?, category = ?, expiration_date = ? "
                        + "WHERE id = ?";

        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement updateStatement = dbConnection.prepareStatement(updateProductSql)) {

            updateStatement.setString(1, product.getName());
            updateStatement.setDouble(2, product.getPrice());
            updateStatement.setInt(3, product.getQuantityInStock());
            updateStatement.setString(4, product.getCategory().name());
            updateStatement.setDate(5, Date.valueOf(product.getExpirationDate()));
            updateStatement.setInt(6, product.getId());

            int affectedRows = updateStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new EntityNotFoundException(
                        "Product not found for update, ID=" + product.getId()
                );
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error updating product", sqlException);
        }
    }


    /**
     * Deletes a product from the database.
     *
     * @param productId The ID of the product to delete.
     * @throws DAOException            If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    @Override
    public void deleteProduct(int productId) throws DAOException, EntityNotFoundException {
        String deleteProductSql = "DELETE FROM product WHERE id = ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement deleteStatement = dbConnection.prepareStatement(deleteProductSql)) {

            deleteStatement.setInt(1, productId);
            int affectedRows = deleteStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new EntityNotFoundException(
                        "Product not found for deletion, ID=" + productId
                );
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error deleting product", sqlException);
        }
    }


    /**
     * Finds products by their category.
     *
     * @param productCategory The category of the products to find.
     * @return A list of products in the specified category.
     * @throws DAOException If there is an error during the operation.
     */
    @Override
    public List<Product> findByCategory(ProductCategory productCategory) throws DAOException {
        String selectByCategorySql =
                "SELECT id, name, price, qty_in_stock, category, expiration_date "
                        + "FROM product WHERE category = ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectByCategorySql)) {

            selectStatement.setString(1, productCategory.name());
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                List<Product> productsByCategoryList = new ArrayList<>();
                while (resultSet.next()) {
                    productsByCategoryList.add(mapRowToProduct(resultSet));
                }
                return productsByCategoryList;
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error fetching products by category", sqlException);
        }
    }


    /**
     * Finds products by their name.
     *
     * @param searchTerm The term to search for in the product name.
     * @return A list of products containing the specified term in their name.
     * @throws DAOException If there is an error during the operation.
     */
    @Override
    public List<Product> findByNameContaining(String searchTerm) throws DAOException {
        String selectByNameSql =
                "SELECT id, name, price, qty_in_stock, category, expiration_date "
                        + "FROM product WHERE name LIKE ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectByNameSql)) {

            selectStatement.setString(1, "%" + searchTerm + "%");
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                List<Product> matchingProductsList = new ArrayList<>();
                while (resultSet.next()) {
                    matchingProductsList.add(mapRowToProduct(resultSet));
                }
                return matchingProductsList;
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error searching products by name", sqlException);
        }
    }


    /**
     * Finds products with low stock.
     *
     * @param stockThreshold The threshold for low stock.
     * @return A list of products with stock below the specified threshold.
     * @throws DAOException If there is an error during the operation.
     */
    @Override
    public List<Product> findLowStock(int stockThreshold) throws DAOException {
        String selectLowStockSql =
                "SELECT id, name, price, qty_in_stock, category, expiration_date "
                        + "FROM product WHERE qty_in_stock < ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectLowStockSql)) {

            selectStatement.setInt(1, stockThreshold);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                List<Product> lowStockProductsList = new ArrayList<>();
                while (resultSet.next()) {
                    lowStockProductsList.add(mapRowToProduct(resultSet));
                }
                return lowStockProductsList;
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error fetching low stock products", sqlException);
        }
    }


    /**
     * Maps a ResultSet row to a Product object.
     *
     * @param resultSet The ResultSet containing the product data.
     * @return A Product object populated with the data from the ResultSet.
     * @throws SQLException If there is an error accessing the ResultSet.
     */
    private Product mapRowToProduct(ResultSet resultSet) throws SQLException {
        String productName = resultSet.getString("name");
        double productPrice = resultSet.getDouble("price");
        int quantityInStock = resultSet.getInt("qty_in_stock");
        ProductCategory category = ProductCategory.valueOf(resultSet.getString("category"));
        LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();

        Product product = new Product(
                productName,
                productPrice,
                quantityInStock,
                category,
                expirationDate
        );
        product.setId(resultSet.getInt("id"));
        return product;
    }
}
