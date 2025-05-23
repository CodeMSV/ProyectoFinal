// src/main/java/com/carnage/service/ProductService.java
package com.carnage.service;

import com.carnage.dao.productDAO.ProductDAO;
import com.carnage.model.Product;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of Product
     * @throws DAOException if a data access error occurs
     */
    public List<Product> getAllProducts() throws DAOException {
        return productDAO.findAll();
    }

    /**
     * Creates a new product record.
     *
     * @param product the Product to create
     * @throws DAOException if a data access error occurs
     */
    public void createProduct(Product product) throws DAOException {
        productDAO.createProduct(product);
    }

    /**
     * Updates an existing product.
     *
     * @param product the Product with updated fields
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if the product does not exist
     */
    public void updateProduct(Product product) throws DAOException, EntityNotFoundException {
        productDAO.updateProduct(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param productId the ID of the Product to delete
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if no product with this ID exists
     */
    public void deleteProduct(int productId) throws DAOException, EntityNotFoundException {
        productDAO.deleteProduct(productId);
    }
}
