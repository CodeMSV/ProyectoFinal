package com.carnage.model;

import java.time.LocalDate;

public class Invoice {

    private Integer id;
    private Integer saleId;
    private LocalDate invoiceDate;
    private byte[] invoicePdf;

    public Invoice() {
    }

    public Invoice(Integer saleId, LocalDate invoiceDate, byte[] invoicePdf) {
        this.saleId = saleId;
        this.invoiceDate = invoiceDate;
        this.invoicePdf = invoicePdf;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleId() {
        return this.saleId;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public byte[] getInvoicePdf() {
        return this.invoicePdf;
    }
}
