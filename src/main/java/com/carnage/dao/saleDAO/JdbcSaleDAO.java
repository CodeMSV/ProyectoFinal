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

    private final ConnectionDDBB databaseConnectionManager;

    public JdbcSaleDAO(ConnectionDDBB databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }

    /**
     * Inserts a new Sale record into the database and updates the Sale object with its generated ID.
     *
     * @param sale the Sale object containing clientId, date, totalPrice, and paymentMethod
     * @throws DAOException if a database error occurs or no rows are inserted
     */
    @Override
    public void createSale(Sale sale) throws DAOException {
        String insertSaleSql =
                "INSERT INTO sale (client_id, sale_date, total_price, payment_method) VALUES (?, ?, ?, ?)";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement insertStatement = dbConnection.prepareStatement(
                     insertSaleSql, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setInt(1, sale.getClientId());
            insertStatement.setDate(2, Date.valueOf(sale.getDate()));
            insertStatement.setDouble(3, sale.getTotalPrice());
            insertStatement.setString(4, sale.getPaymentMethod().name());

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating sale failed, no rows affected.");
            }

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sale.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating sale failed, no ID obtained.");
                }
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error creating sale", sqlException);
        }
    }

    /**
     * Retrieves a Sale by its ID, including all associated products.
     *
     * @param saleId the ID of the sale to retrieve
     * @return the Sale object populated with its products
     * @throws DAOException            if a database error occurs
     * @throws EntityNotFoundException if no sale exists with the given ID
     */
    @Override
    public Sale findById(int saleId) throws DAOException, EntityNotFoundException {
        String selectSaleByIdSql =
                "SELECT id, client_id, sale_date, total_price, payment_method FROM sale WHERE id = ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectSaleByIdSql)) {

            selectStatement.setInt(1, saleId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new EntityNotFoundException("Sale not found with ID=" + saleId);
                }
                Sale sale = mapRowToSale(resultSet);
                sale.setProducts(findProductsBySaleId(sale.getId()));
                return sale;
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error finding sale by ID", sqlException);
        }
    }

    /**
     * Retrieves all Sale records from the database, each with its associated products.
     *
     * @return a list of all Sale objects
     * @throws DAOException if a database error occurs
     */
    @Override
    public List<Sale> findAll() throws DAOException {
        String selectAllSalesSql =
                "SELECT id, client_id, sale_date, total_price, payment_method FROM sale";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectAllSalesSql);
             ResultSet resultSet = selectStatement.executeQuery()) {

            List<Sale> salesList = new ArrayList<>();
            while (resultSet.next()) {
                Sale sale = mapRowToSale(resultSet);
                sale.setProducts(findProductsBySaleId(sale.getId()));
                salesList.add(sale);
            }
            return salesList;

        } catch (SQLException sqlException) {
            throw new DAOException("Error fetching all sales", sqlException);
        }
    }

    /**
     * Retrieves all Sale records for a given client ID, each with its associated products.
     *
     * @param clientId the ID of the client whose sales to fetch
     * @return a list of Sale objects for the specified client
     * @throws DAOException if a database error occurs
     */
    @Override
    public List<Sale> findByClientId(int clientId) throws DAOException {
        String selectSalesByClientSql =
                "SELECT id, client_id, sale_date, total_price, payment_method FROM sale WHERE client_id = ?";
        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectSalesByClientSql)) {

            selectStatement.setInt(1, clientId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                List<Sale> salesByClientList = new ArrayList<>();
                while (resultSet.next()) {
                    Sale sale = mapRowToSale(resultSet);
                    sale.setProducts(findProductsBySaleId(sale.getId()));
                    salesByClientList.add(sale);
                }
                return salesByClientList;
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error fetching sales by client", sqlException);
        }
    }

    /**
     * Maps the current row of the given ResultSet to a Sale object (without products).
     *
     * @param resultSet the ResultSet positioned at the desired row
     * @return a Sale object with id, clientId, date, totalPrice, and paymentMethod set
     * @throws SQLException if a database access error occurs
     */
    private Sale mapRowToSale(ResultSet resultSet) throws SQLException {
        int saleId = resultSet.getInt("id");
        int clientId = resultSet.getInt("client_id");
        LocalDate saleDate = resultSet.getDate("sale_date").toLocalDate();
        double totalPrice = resultSet.getDouble("total_price");
        PaymentMethod method = PaymentMethod.valueOf(resultSet.getString("payment_method"));

        Sale sale = new Sale(clientId, new ArrayList<>(), totalPrice, saleDate, method);
        sale.setId(saleId);
        return sale;
    }

    /**
     * Loads all products for a given sale, repeating each product according to its quantity.
     *
     * @param saleId the ID of the sale whose items to load
     * @return a list of Products, repeated by quantity sold
     * @throws DAOException if a database error occurs
     */
    private List<Product> findProductsBySaleId(int saleId) throws DAOException {
        String selectSaleItemsSql =
                "SELECT p.id, p.name, si.quantity, si.price_at_sale " +
                        "FROM sale_item si " +
                        "JOIN product p ON si.product_id = p.id " +
                        "WHERE si.sale_id = ?";
        List<Product> productsList = new ArrayList<>();

        try (Connection dbConnection = databaseConnectionManager.getConnection();
             PreparedStatement selectStatement = dbConnection.prepareStatement(selectSaleItemsSql)) {

            selectStatement.setInt(1, saleId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product(
                            resultSet.getString("name"),
                            resultSet.getDouble("price_at_sale"),
                            0, null, null
                    );
                    product.setId(resultSet.getInt("id"));
                    int quantity = resultSet.getInt("quantity");
                    for (int i = 0; i < quantity; i++) {
                        productsList.add(product);
                    }
                }
            }

        } catch (SQLException sqlException) {
            throw new DAOException("Error loading sale items for sale " + saleId, sqlException);
        }

        return productsList;
    }
}
