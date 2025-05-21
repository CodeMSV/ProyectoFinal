package com.carnage.dao.userDAO;

import com.carnage.util.dao.DAOException;
import com.carnage.model.user.User;
import com.carnage.model.user.client.Client;
import com.carnage.util.dao.EntityNotFoundException;

import java.util.List;

public interface UserDAO {

    User create(User user) throws DAOException;

    User findById(int id) throws DAOException, EntityNotFoundException;

    User findByEmail(String email) throws DAOException, EntityNotFoundException;

    List<User> findAll() throws DAOException;

    User update(User user) throws DAOException, EntityNotFoundException;

    void delete(int id) throws DAOException, EntityNotFoundException;

    User authenticate(String email, String password) throws DAOException, EntityNotFoundException;

    List<Client> findAllClients() throws DAOException;



}
