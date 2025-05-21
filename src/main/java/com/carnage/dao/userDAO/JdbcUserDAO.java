package com.carnage.dao.userDAO;

import com.carnage.model.user.User;
import com.carnage.model.user.admin.Admin;
import com.carnage.model.user.client.Client;
import com.carnage.util.DDBB.ConnectionDDBB;
import com.carnage.util.dao.DAOException;
import com.carnage.util.dao.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDAO implements UserDAO {

    private final ConnectionDDBB cm;

    public JdbcUserDAO(ConnectionDDBB cm) {
        this.cm = cm;
    }

    @Override
    public User authenticate(String email, String password)
            throws DAOException, EntityNotFoundException {

        String sql = "SELECT id, user_name, user_email, user_password, address, phone, role " +
                "FROM users WHERE user_email = ? AND user_password = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("Invalid credentials for email: " + email);
                }

                int     id       = rs.getInt("id");
                String  name     = rs.getString("user_name");
                String  mail     = rs.getString("user_email");
                String  pwd      = rs.getString("user_password");
                String  role     = rs.getString("role");

                User user;
                if ("ADMIN".equalsIgnoreCase(role)) {
                    user = new Admin(name, mail, pwd);
                } else {
                    String addr = rs.getString("address");
                    String    phone = rs.getString("phone");
                    user = new Client(name, mail, pwd, addr, phone);
                }
                user.setId(id);
                return user;
            }
        } catch (SQLException ex) {
            throw new DAOException("Error authenticating user", ex);
        }
    }

    @Override
    public List<Client> findAllClients() throws DAOException {
        String sql = "SELECT id, user_name, user_email, user_password, address, phone " +
                "FROM users WHERE role = 'CLIENT'";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                Client c = new Client(
                        rs.getString("user_name"),
                        rs.getString("user_email"),
                        rs.getString("user_password"),
                        rs.getString("address"),
                        rs.getString("phone")
                );
                c.setId(rs.getInt("id"));
                clients.add(c);
            }
            return clients;
        } catch (SQLException ex) {
            throw new DAOException("Error fetching all clients", ex);
        }
    }

    @Override
    public User create(User user) throws DAOException {
        String sql = "INSERT INTO users " +
                "(user_name, user_email, user_password, address, phone, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getUserPassword());

            if (user instanceof Client) {
                Client c = (Client) user;
                ps.setString(4, c.getAddress());
                ps.setString(5, c.getPhone());
                ps.setString(6, "CLIENT");
            } else {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.INTEGER);
                ps.setString(6, "ADMIN");
            }

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                } else {
                    throw new DAOException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        } catch (SQLException ex) {
            throw new DAOException("Error creating user", ex);
        }
    }

    @Override
    public User findById(int id) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, user_name, user_email, user_password, address, phone, role " +
                "FROM users WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("User not found with ID=" + id);
                }
                return mapRowToUser(rs);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error finding user by ID", ex);
        }
    }

    @Override
    public User findByEmail(String email) throws DAOException, EntityNotFoundException {
        String sql = "SELECT id, user_name, user_email, user_password, address, phone, role " +
                "FROM users WHERE user_email = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntityNotFoundException("User not found with email=" + email);
                }
                return mapRowToUser(rs);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error finding user by email", ex);
        }
    }

    @Override
    public List<User> findAll() throws DAOException {
        String sql = "SELECT id, user_name, user_email, user_password, address, phone, role FROM users";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;
        } catch (SQLException ex) {
            throw new DAOException("Error fetching all users", ex);
        }
    }

    @Override
    public User update(User user) throws DAOException, EntityNotFoundException {
        String sql = "UPDATE users SET user_name = ?, user_email = ?, user_password = ?, " +
                "address = ?, phone = ?, role = ? WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getUserPassword());
            if (user instanceof Client) {
                ps.setString(4, ((Client) user).getAddress());
                ps.setString(5, ((Client) user).getPhone());
                ps.setString(6, "CLIENT");
            } else {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.INTEGER);
                ps.setString(6, "ADMIN");
            }
            ps.setInt(7, user.getId());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new EntityNotFoundException("User not found for update, ID=" + user.getId());
            }
            return user;
        } catch (SQLException ex) {
            throw new DAOException("Error updating user", ex);
        }
    }

    @Override
    public void delete(int id) throws DAOException, EntityNotFoundException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = cm.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new EntityNotFoundException("User not found for deletion, ID=" + id);
            }
        } catch (SQLException ex) {
            throw new DAOException("Error deleting user", ex);
        }
    }

    /** Mapea la fila actual del ResultSet a un objeto User concreto */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        int     id    = rs.getInt("id");
        String  name  = rs.getString("user_name");
        String  mail  = rs.getString("user_email");
        String  pwd   = rs.getString("user_password");
        String  role  = rs.getString("role");

        User user;
        if ("ADMIN".equalsIgnoreCase(role)) {
            user = new Admin(name, mail, pwd);
        } else {
            String addr = rs.getString("address");
            String    phone = rs.getString("phone");
            user = new Client(name, mail, pwd, addr, phone);
        }
        user.setId(id);
        return user;
    }
}
