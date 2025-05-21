package com.carnage.dao.userDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.user.client.Client;

public interface ClientDAO {

    /**
     * Creates a new client in the database.
     *
     * @param client The client to create.
     * @return The created client.
     * @throws DAOException If an error occurs while creating the client.
     */
    Client findByUserId(int userId) throws DAOException, EntityNotFoundException;

}
