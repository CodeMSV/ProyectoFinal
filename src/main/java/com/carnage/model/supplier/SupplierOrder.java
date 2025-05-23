package com.carnage.model.supplier;

import com.carnage.model.sale.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SupplierOrder {

    private Integer id;

    private LocalDateTime createdAt;
    private List<SupplierOrderItem> items;
    private OrderStatus status;


    public SupplierOrder() {
    }

    public SupplierOrder(List<SupplierOrderItem> items, OrderStatus status) {
        this.items = items;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public List<SupplierOrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SupplierOrder that = (SupplierOrder) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupplierOrder{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
