package com.carnage.dao.userDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.model.user.User;
import com.carnage.model.user.client.Client;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public interface UserDAO {

    /**
     * Creates a new user in the database.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws DAOException If there is an error during the operation.
     */
    User create(User user) throws DAOException;

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The found user.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    User findById(int id) throws DAOException, EntityNotFoundException;

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return The found user.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    User findByEmail(String email) throws DAOException, EntityNotFoundException;

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user to find.
     * @return The found user.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    List<User> findAll() throws DAOException;

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to update.
     * @return The updated user.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    User update(User user) throws DAOException, EntityNotFoundException;

    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    void delete(int id) throws DAOException, EntityNotFoundException;

    /**
     * Authenticates a user by their email and password.
     *
     * @param email The email of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The authenticated user.
     * @throws DAOException If there is an error during the operation.
     * @throws EntityNotFoundException If the user is not found.
     */
    User authenticate(String email, String password) throws DAOException, EntityNotFoundException;

    List<Client> findAllClients() throws DAOException;



}
