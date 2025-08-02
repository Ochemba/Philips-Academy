package com.se.philips;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class
UserService {
    private Connection connection;
    private boolean dbAvailable = false;
    private List<User> fallbackUsers = new ArrayList<>();
    private int nextId = 1;

    public UserService() {
        initializeDatabaseConnection();
        initializeFallbackData();
    }

    private void initializeDatabaseConnection() {
        try {
            // Update these with your actual credentials
            String url = "jdbc:mysql://localhost:3306/research";
            String user = "root";
            String password = "Blue1234."; // Empty password by default

            // Additional parameters for MySQL 8+
            url += "?useSSL=false&allowPublicKeyRetrieval=true";

            connection = DriverManager.getConnection(url, user, password);
            dbAvailable = true;
            System.out.println("Database connected successfully!");

            // Verify table exists
            ensureTableExists();
        } catch (SQLException e) {
            System.err.println("Database connection failed. Using in-memory fallback.");
            System.err.println("Error: " + e.getMessage());
            connection = null;
            dbAvailable = false;
        }
    }

    private void ensureTableExists() throws SQLException {
        if (!dbAvailable) return;

        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "role VARCHAR(50) NOT NULL)";
            stmt.executeUpdate(sql);
        }
    }

    private void initializeFallbackData() {
        fallbackUsers.add(new User(nextId++, "Admin (Fallback)", "admin@fallback.com", "ADMIN"));
        fallbackUsers.add(new User(nextId++, "User (Fallback)", "user@fallback.com", "USER"));
    }

    // CREATE
    public User createUser(User user) {
        if (dbAvailable) {
            String sql = "INSERT INTO users (name, email, role) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getRole());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        return user;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Failed to create user in database: " + e.getMessage());
            }
        }

        // Fallback to in-memory
        user.setId(nextId++);
        fallbackUsers.add(user);
        return user;
    }

    // READ ALL
    public List<User> getAllUsers() {
        if (dbAvailable) {
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM users";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("role")
                    ));
                }
                return users;
            } catch (SQLException e) {
                System.err.println("⚠️ Failed to fetch users from database: " + e.getMessage());
            }
        }
        return new ArrayList<>(fallbackUsers);
    }

    // UPDATE
    public boolean updateUser(User updatedUser) {
        if (dbAvailable) {
            String sql = "UPDATE users SET name=?, email=?, role=? WHERE id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, updatedUser.getName());
                stmt.setString(2, updatedUser.getEmail());
                stmt.setString(3, updatedUser.getRole());
                stmt.setInt(4, updatedUser.getId());
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("⚠️ Failed to update user in database: " + e.getMessage());
            }
        }

        // Fallback to in-memory
        for (int i = 0; i < fallbackUsers.size(); i++) {
            if (fallbackUsers.get(i).getId() == updatedUser.getId()) {
                fallbackUsers.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean deleteUser(int id) {
        if (dbAvailable) {
            String sql = "DELETE FROM users WHERE id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("⚠️ Failed to delete user from database: " + e.getMessage());
            }
        }

        // Fallback to in-memory
        return fallbackUsers.removeIf(u -> u.getId() == id);
    }

    // Utility method to check connection status
    public boolean isDatabaseAvailable() {
        return dbAvailable;
    }
}