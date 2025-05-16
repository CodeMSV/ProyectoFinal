package com.carnage.model;

import java.time.LocalDate;

public class Invoice {

    private Integer id;
    private static Integer InvoiceId = 0;
    private Integer orderId;
    private LocalDate invoiceDate;
    private byte[] invoicePdf;


    public Invoice(Integer orderId, LocalDate invoiceDate, byte[] invoicePdf) {
        this.id = InvoiceId++;
        this.orderId = orderId;
        this.invoiceDate = invoiceDate;
        this.invoicePdf = invoicePdf;
    }
}
