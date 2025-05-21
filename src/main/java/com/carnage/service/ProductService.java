package com.carnage.service;

import com.carnage.dao.productDAO.ProductDAO;
import com.carnage.model.Product;
import com.carnage.util.dao.DAOException;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllProducts() throws DAOException {
        return productDAO.findAll();
    }
}
