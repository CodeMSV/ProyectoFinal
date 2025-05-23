package com.carnage.model;

import com.carnage.model.Product;
import com.carnage.model.user.client.PaymentMethod;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Sale {

    private Integer id;
    private Integer clientId;
    private List<Product> products;
    private Double totalPrice;
    private LocalDate date;
    private PaymentMethod paymentMethod;

    public Sale() {
    }

    public Sale(Integer clientId, List<Product> products, Double totalPrice, LocalDate date, PaymentMethod paymentMethod) {
        this.clientId = clientId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {  // <— nuevo setter
        this.products = products;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", products=" + products +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
