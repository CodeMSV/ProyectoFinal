package com.carnage.dao.supplierDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.model.supplier.SupplierOrderItem;

import java.util.List;

public interface SupplierOrderItemDAO {

    /**
     * Creates a new supplier order item in the database.
     *
     * @param item The supplier order item to create.
     * @throws DAOException If there is an error during the operation.
     */
    void createItem(SupplierOrderItem item) throws DAOException;

    /**
     * Finds a supplier order item by its ID.
     *
     * @param itemId The ID of the supplier order item to find.
     * @return The found supplier order item.
     * @throws DAOException If there is an error during the operation.
     */
    List<SupplierOrderItem> findByOrderId(int orderId) throws DAOException;


}
