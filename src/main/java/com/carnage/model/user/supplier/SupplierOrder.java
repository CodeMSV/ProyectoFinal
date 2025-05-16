package com.carnage.model.user.supplier;

import com.carnage.model.sale.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class SupplierOrder {

    private Integer id;
    private static Integer identifierCounter = 0;
    private Integer adminId;
    private LocalDateTime fecha;
    private List<SupplierOrderItem> items;
    private OrderStatus status;

    public SupplierOrder(Integer adminId, List<SupplierOrderItem> items, OrderStatus status) {
        this.id = identifierCounter++;
        this.adminId = adminId;
        this.fecha = LocalDateTime.now();
        this.items = items;
        this.status = status;
    }
}
