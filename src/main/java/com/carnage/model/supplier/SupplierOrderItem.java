package com.carnage.model.supplier;

import java.util.Objects;

public class SupplierOrderItem {

    private Integer id;
    private Integer productId;
    private int quantity;
    private Integer supplierOrderId;


    public SupplierOrderItem(Integer productId, int quantity, Integer supplierOrder) {
        this.productId = productId;
        this.quantity = quantity;
        this.supplierOrderId = supplierOrder;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Integer getSupplierOrder() {
        return this.supplierOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SupplierOrderItem that = (SupplierOrderItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupplierOrderItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", supplierOrderId=" + supplierOrderId +
                '}';
    }
}
