package com.exp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.exp.model.Expense;
import com.exp.util.DatabaseConnection;

public class ExpenseDAO {

    // ✅ Create a new expense (skip exp_id and exp_date)
    public void createExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (title, description, amt, id) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, expense.getTitle());
            ps.setString(2, expense.getDescription());
            ps.setDouble(3, expense.getAmount());
            ps.setInt(4, expense.getCategoryId());
            ps.executeUpdate();
        }
    }

    // ✅ Update an existing expense (don’t touch exp_id, only update fields)
    public boolean updateExpense(Expense expense) throws SQLException {
        String sql = "UPDATE expenses SET title=?, description=?, amt=?, id=? WHERE exp_id=?";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, expense.getTitle());
            ps.setString(2, expense.getDescription());
            ps.setDouble(3, expense.getAmount());
            ps.setInt(4, expense.getCategoryId());
            ps.setInt(5, expense.getId());  // this is exp_id
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Delete an expense by exp_id
    public boolean deleteExpense(int expId) throws SQLException {
        String sql = "DELETE FROM expenses WHERE exp_id=?";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, expId);
            return ps.executeUpdate() > 0;
        }
    }

    // ✅ Get a single expense by exp_id
    public Expense getExpenseById(int expId) throws SQLException {
        String sql = "SELECT exp_id, title, description, amt, exp_date, id FROM expenses WHERE exp_id=?";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, expId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Expense(
                            rs.getInt("exp_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("amt"),
                            rs.getString("exp_date"),
                            rs.getInt("id")
                    );
                }
            }
        }
        return null;
    }

    // ✅ Get all expenses
    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT exp_id, title, description, amt, exp_date, id FROM expenses";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Expense exp = new Expense(
                        rs.getInt("exp_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("amt"),
                        rs.getString("exp_date"),
                        rs.getInt("id")
                );
                list.add(exp);
            }
        }
        return list;
    }

    public List<Expense> getExpensesByCategory(int categoryId) throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT exp_id, title, description, amt, exp_date, id FROM expenses WHERE id=?";
        try (Connection con = DatabaseConnection.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Expense(
                        rs.getInt("exp_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("amt"),
                        rs.getString("exp_date"),
                        rs.getInt("id")
                    ));
                }
            }
        }
        return list;
    }


    // ✅ TableModel for GUI JTable
    public DefaultTableModel getExpenseTableModel() throws SQLException {
        String[] columns = {"Expense ID", "Title", "Description", "Amount", "Date", "Category ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String sql = "SELECT exp_id, title, description, amt, exp_date, id FROM expenses";
        try (Connection con = DatabaseConnection.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("exp_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("amt"),
                        rs.getString("exp_date"),
                        rs.getInt("id")
                };
                model.addRow(row);
            }
        }
        return model;
    }
}
