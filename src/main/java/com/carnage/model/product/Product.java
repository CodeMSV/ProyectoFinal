package com.carnage.model;

import com.carnage.model.product.ProductCategory;

import java.time.LocalDate;
import java.util.Objects;

public class Product {

    private Integer id;
    private String name;
    private Double price;
    private int quantityInStock;
    private ProductCategory category;
    private LocalDate expirationDate;

    public Product() {}

    public Product(String name, Double price, int quantityInStock, ProductCategory category, LocalDate expirationDate) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Product{" + id + ", " + name + '}';
    }
}
