package com.carnage.model.user.supplier;

public class SupplierOrderItem {

    private Integer id;
    private static Integer identifierCounter = 0;
    private Integer productId;
    private int quantity;
    private SupplierOrder supplierOrder;


    public SupplierOrderItem(Integer productId, int quantity, SupplierOrder supplierOrder) {
        this.id = identifierCounter++;
        this.productId = productId;
        this.quantity = quantity;
        this.supplierOrder = supplierOrder;
    }

}
