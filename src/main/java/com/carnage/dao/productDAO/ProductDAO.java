package com.carnage.dao.productDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.Product;
import com.carnage.model.product.ProductCategory;

import java.util.List;

public interface ProductDAO {


    /**
     * Creates a new product in the database.
     *
     * @param product The product to create.
     * @throws DAOException If there is an error during the operation.
     */
    void createProduct(Product product) throws DAOException;

    /**
     * Finds a product by its ID.
     *
     * @param productId The ID of the product to find.
     * @return An Optional containing the found product, or an empty Optional if not found.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    Product findById(int productId) throws DAOException, EntityNotFoundException;

    /**
     * Finds a product by its name.
     *
     * @param name The name of the product to find.
     * @return An Optional containing the found product, or an empty Optional if not found.
     * @throws DAOException If there is an error during the operation.
     */
    List<Product> findAll() throws DAOException;

    /**
     * Updates an existing product in the database.
     *
     * @param product The product to update.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    void updateProduct(Product product) throws DAOException, EntityNotFoundException;

    /**
     * Deletes a product from the database.
     *
     * @param productId The ID of the product to delete.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the product is not found.
     */
    void deleteProduct(int productId) throws DAOException, EntityNotFoundException;

    /**
     * Finds products by their category.
     *
     * @param category The category of the products to find.
     * @return A list of products in the specified category.
     * @throws DAOException If there is an error during the operation.
     */
    List<Product> findByCategory(ProductCategory category) throws DAOException;

    /**
     * Finds products by their name.
     *
     * @param term The term to search for in the product name.
     * @return A list of products containing the specified term in their name.
     * @throws DAOException If there is an error during the operation.
     */
    List<Product> findByNameContaining(String term) throws DAOException;

    /**
     * Finds products with low stock.
     *
     * @param threshold The stock threshold to check against.
     * @return A list of products with stock below the specified threshold.
     * @throws DAOException If there is an error during the operation.
     */
    List<Product> findLowStock(int threshold) throws DAOException;

}
