package com.carnage.dao.supplierDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.supplier.SupplierOrder;

import java.util.List;

public interface SupplierOrderDAO {


    /**
     * Creates a new supplier order in the database.
     *
     * @param order The supplier order to create.
     * @throws DAOException If there is an error during the operation.
     */
    void createOrder(SupplierOrder order) throws DAOException;

    /**
     * Finds a supplier order by its ID.
     *
     * @param orderId The ID of the supplier order to find.
     * @return The found supplier order.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the supplier order is not found.
     */
    SupplierOrder findById(int orderId) throws DAOException, EntityNotFoundException;

    /**
     * Finds all supplier orders in the database.
     *
     * @return A list of all supplier orders.
     * @throws DAOException If there is an error during the operation.
     */
    List<SupplierOrder> findAll() throws DAOException;

}
