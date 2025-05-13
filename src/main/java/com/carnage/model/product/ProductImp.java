package com.carnage.model.product;

import java.time.LocalDate;

public class ProductImp implements Product{

    private Integer idProduct;
    private static Integer idCounter = 0;
    private String name;
    private String meatType;
    private String origin;
    private LocalDate expirationDate;
    private Double weight;
    private Double price;
    private Integer stock;
    private boolean expired;

    public ProductImp(String name, String meatType, String origin, LocalDate expirationDate, Double weight, Double price, Integer stock) {
        this.idProduct = idCounter++;
        this.name = name;
        this.meatType = meatType;
        this.origin = origin;
        this.expirationDate = expirationDate;
        this.weight = weight;
        this.price = price;
        this.stock = stock;
        this.expired = false;
    }

    @Override
    public Integer getIdProduct() {
        return this.idProduct;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getMeatType() {
        return this.meatType;
    }

    @Override
    public String getOrigin() {
        return this.origin;
    }

    @Override
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public Double getWeight() {
        return this.weight;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }

    @Override
    public Integer getStock() {
        return this.stock;
    }

    @Override
    public void updateStock(Integer amount) {
        // TODO: Implement this method
    }

    @Override
    public boolean isExpired() {
        //TODO: Implement this method

        return false;
    }
}
