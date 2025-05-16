package com.carnage.model;

import com.carnage.model.product.ProductCategory;

import java.time.LocalDate;
import java.util.Objects;

public class Product {

    private Integer identifier;
    private static Integer identifierCounter = 0;
    private String name;
    private Double price;
    private int quantityInStock;
    private ProductCategory category;
    private LocalDate expirationDate;


    Product(String name, Double price, int quantityInStock, ProductCategory category, LocalDate expirationDate) {
        this.identifier = identifierCounter++;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.category = category;
        this.expirationDate = expirationDate;
    }


    public Integer getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        return this.name;
    }

    public Double getPrice() {
        return this.price;
    }

    public int getQuantityInStock() {
        return this.quantityInStock;
    }

    public ProductCategory getCategory() {
        return this.category;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    void assignIdentifier(Integer id) {
        this.identifier = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product p)) return false;
        return Objects.equals(identifier, p.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "Product{" + identifier + ", " + name + "}";
    }
}
