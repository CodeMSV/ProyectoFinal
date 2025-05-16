package com.carnage.model;

import com.carnage.model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Sale {

    private Integer id;
    private static Integer identifierCounter = 0;
    private List<Product> products;
    private Double totalPrice;
    private LocalDate date;
    private PaymentMethod paymentMethod;

}
