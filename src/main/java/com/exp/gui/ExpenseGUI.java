package com.exp.gui;

import com.exp.dao.ExpenseDAO;
import com.exp.model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ExpenseGUI extends JFrame {

    private JTextField txtId, txtTitle, txtDesc, txtAmount;
    private JComboBox<String> cmbCategory;
    private JTable table;
    private DefaultTableModel model;
    private ExpenseDAO expenseDAO;

    public ExpenseGUI() {
        super("Expense Manager");
        expenseDAO = new ExpenseDAO();

        setLayout(new BorderLayout());

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        // ID (only for update/delete)
        inputPanel.add(new JLabel("ID:"));
        txtId = new JTextField();
        inputPanel.add(txtId);

        // Title
        inputPanel.add(new JLabel("Title:"));
        txtTitle = new JTextField();
        inputPanel.add(txtTitle);

        // Description
        inputPanel.add(new JLabel("Description:"));
        txtDesc = new JTextField();
        inputPanel.add(txtDesc);

        // Amount
        inputPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        inputPanel.add(txtAmount);

        // Category dropdown
        inputPanel.add(new JLabel("Category:"));
        String[] categories = {"Grocery", "Transport", "Shopping", "Bills", "Entertainment"};
        cmbCategory = new JComboBox<>(categories);
        inputPanel.add(cmbCategory);

        add(inputPanel, BorderLayout.NORTH);

        // ===== Table Panel =====
        model = new DefaultTableModel(new String[]{"ID","Title","Description","Amount","Date","Category"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Button Panel =====
        JPanel btnPanel = new JPanel();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        btnAdd.addActionListener(e -> createExpense());
        btnUpdate.addActionListener(e -> updateExpense());
        btnDelete.addActionListener(e -> deleteExpense());
        btnRefresh.addActionListener(e -> loadExpenses());

        // ===== Table Row Click =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtTitle.setText(model.getValueAt(row, 1).toString());
                    txtDesc.setText(model.getValueAt(row, 2).toString());
                    txtAmount.setText(model.getValueAt(row, 3).toString());

                    // Category (DB stores numeric ID → map to dropdown)
                    int catId = Integer.parseInt(model.getValueAt(row, 5).toString());
                    cmbCategory.setSelectedIndex(catId - 1);
                }
            }
        });

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initial load
        loadExpenses();
    }

    private void createExpense() {
        try {
            Expense e = new Expense(
                0,
                txtTitle.getText(),
                txtDesc.getText(),
                Double.parseDouble(txtAmount.getText()),
                null, // date is auto
                cmbCategory.getSelectedIndex() + 1 // map dropdown to ID
            );
            expenseDAO.createExpense(e);
            loadExpenses();
            clearFields();
            JOptionPane.showMessageDialog(this, "Expense added successfully!");
        } catch (SQLException ex) {
            showError(ex);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        }
    }

    private void updateExpense() {
        try {
            Expense e = new Expense(
                Integer.parseInt(txtId.getText()),
                txtTitle.getText(),
                txtDesc.getText(),
                Double.parseDouble(txtAmount.getText()),
                null, // don't update date
                cmbCategory.getSelectedIndex() + 1
            );
            if (expenseDAO.updateExpense(e)) {
                loadExpenses();
                clearFields();
                JOptionPane.showMessageDialog(this, "Expense updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Expense not found.");
            }
        } catch (SQLException ex) {
            showError(ex);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void deleteExpense() {
        try {
            int id = Integer.parseInt(txtId.getText());
            if (expenseDAO.deleteExpense(id)) {
                loadExpenses();
                clearFields();
                JOptionPane.showMessageDialog(this, "Expense deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Expense not found.");
            }
        } catch (SQLException ex) {
            showError(ex);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
        }
    }

    private void loadExpenses() {
        try {
            model.setRowCount(0);
            List<Expense> list = expenseDAO.getAllExpenses();
            for (Expense e : list) {
                model.addRow(new Object[]{
                        e.getId(), e.getTitle(), e.getDescription(),
                        e.getAmount(), e.getDate(), e.getCategoryId()
                });
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtTitle.setText("");
        txtDesc.setText("");
        txtAmount.setText("");
        cmbCategory.setSelectedIndex(0);
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseGUI().setVisible(true));
    }
}
