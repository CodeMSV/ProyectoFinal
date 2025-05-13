package com.carnage.model.sale;

import com.carnage.model.product.Product;

import java.time.LocalDate;
import java.util.List;

public class SaleImp implements Sale {

    private Integer idSale;
    private static Integer idCounter = 0;
    private List<Product> products;
    private Double totalPrice;
    private LocalDate date;
    private PaymentMethod paymentMethod;


    public SaleImp(List<Product> products, Double totalPrice, LocalDate date, PaymentMethod paymentMethod) {
        this.idSale = ++idCounter;
        this.products = products;
        this.totalPrice = totalPrice;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public Double calculateTotalPrice() {
        return 0.0;
    }

    @Override
    public Integer getIdSale() {
        return this.idSale;
    }

    @Override
    public List<Product> getProducts() {
        return this.products;
    }

    @Override
    public Double getTotalPrice() {
        return this.totalPrice;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }
}
