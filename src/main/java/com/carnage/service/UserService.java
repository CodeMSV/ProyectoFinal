// src/main/java/com/carnage/service/UserService.java
package com.carnage.service;

import com.carnage.dao.userDAO.UserDAO;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;
import com.carnage.model.user.User;
import com.carnage.model.user.client.Client;

import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return the authenticated User
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if authentication fails or user not found
     */
    public User authenticate(String email, String password)
            throws DAOException, EntityNotFoundException {
        return userDAO.authenticate(email, password);
    }

    /**
     * Registers a new client in the system.
     *
     * @param client the Client object to create
     * @return the created Client with assigned ID
     * @throws DAOException if a data access error occurs
     */
    public Client registerClient(Client client) throws DAOException {
        // Since Client extends User and UserDAO.create(User) handles creation:
        User created = userDAO.create(client);
        return (Client) created;
    }

    /**
     * Retrieves a client by their ID.
     *
     * @param id the ID of the client
     * @return the Client object
     * @throws DAOException            if a data access error occurs
     * @throws EntityNotFoundException if the client is not found
     */
    public List<Client> listAllClients() throws DAOException {
        return userDAO.findAllClients();
    }
}
