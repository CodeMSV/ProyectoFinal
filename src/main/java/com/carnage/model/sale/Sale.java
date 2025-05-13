package com.carnage.model.sale;

import com.carnage.model.product.Product;

import java.time.LocalDate;
import java.util.List;

public interface Sale {

    Double calculateTotalPrice();
    Integer getIdSale();
    List<Product> getProducts();
    Double getTotalPrice();
    LocalDate getDate();
    PaymentMethod getPaymentMethod();
}
