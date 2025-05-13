package com.carnage.model.product;

import java.time.LocalDate;

public interface Product {
    Integer getIdProduct();
    String getName();
    String getMeatType();
    String getOrigin();
    LocalDate getExpirationDate();
    Double getWeight();
    Double getPrice();
    Integer getStock();
    void updateStock(Integer amount);
    boolean isExpired();
}
