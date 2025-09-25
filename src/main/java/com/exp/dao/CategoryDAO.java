package com.exp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.exp.model.Category;
import com.exp.util.DatabaseConnection;

public class CategoryDAO {

    // SQL Queries
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category ORDER BY id ASC";
    private static final String INSERT_CATEGORY = "INSERT INTO category(name) VALUES(?)";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE category SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";

    // 1️⃣ Create a new category
    public int createCategory(Category category) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating category failed, no row inserted");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("Creating category failed, no ID obtained");
                }
            }
        }
    }

    // 2️⃣ Get category by ID
    public Category getCategoryById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORY_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getCategoryFromResultSet(rs);
                }
            }
        }
        return null; // Not found
    }

    // 3️⃣ Get all categories
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(getCategoryFromResultSet(rs));
            }
        }
        return categories;
    }

    // 4️⃣ Update category
    public boolean updateCategory(Category category) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CATEGORY)) {

            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // 5️⃣ Delete category
    public boolean deleteCategory(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Helper method: convert ResultSet row into Category object
    private Category getCategoryFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Category(id, name);
    }
}
