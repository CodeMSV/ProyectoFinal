package com.carnage.service;

import com.carnage.dao.supplierDAO.SupplierOrderDAO;
import com.carnage.dao.supplierDAO.SupplierOrderItemDAO;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.supplier.SupplierOrder;

import java.util.List;

public class SupplierOrderService {

    private SupplierOrderDAO orderDAO;
    private SupplierOrderItemDAO itemDAO;

    /**
     * Constructs a SupplierOrderService.
     *
     * @param orderDAO DAO for supplier orders
     * @param itemDAO  DAO for order items
     */
    public SupplierOrderService(SupplierOrderDAO orderDAO, SupplierOrderItemDAO itemDAO) {
        this.orderDAO = orderDAO;
        this.itemDAO = itemDAO;
    }

    /**
     * Retrieves all supplier orders.
     *
     * @return list of SupplierOrder
     * @throws DAOException if a data access error occurs
     */
    public List<SupplierOrder> getAllOrders() throws DAOException {
        return orderDAO.findAll();
    }

    /**
     * Retrieves a supplier order by its ID.
     *
     * @param orderId the order identifier
     * @return the SupplierOrder
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if the order is not found
     */
    public SupplierOrder getOrderById(int orderId)
            throws DAOException, EntityNotFoundException {
        return orderDAO.findById(orderId);
    }


}
