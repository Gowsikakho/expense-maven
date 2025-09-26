package com.exp.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.exp.dao.CategoryDAO;
import com.exp.dao.ExpenseDAO;
import com.exp.model.Expense;

public class ExpenseGUI extends JFrame {

    private JTextField txtId, txtTitle, txtDesc, txtAmount;
    private JComboBox<String> cmbCategory;
    private JTable table;
    private DefaultTableModel model;
    private ExpenseDAO expenseDAO;
    private CategoryDAO categoryDAO;

    public ExpenseGUI() {
        super("Expense Manager");
        expenseDAO = new ExpenseDAO();
        categoryDAO = new CategoryDAO();

        setLayout(new BorderLayout());

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        inputPanel.add(new JLabel("ID:"));
        txtId = new JTextField();
        inputPanel.add(txtId);

        inputPanel.add(new JLabel("Title:"));
        txtTitle = new JTextField();
        inputPanel.add(txtTitle);

        inputPanel.add(new JLabel("Description:"));
        txtDesc = new JTextField();
        inputPanel.add(txtDesc);

        inputPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        inputPanel.add(txtAmount);

        inputPanel.add(new JLabel("Category:"));
        List<String> categoryList = categoryDAO.getAllCategoryNames();
        cmbCategory = new JComboBox<>(categoryList.toArray(new String[0]));
        inputPanel.add(cmbCategory);

        // ===== Button Panel =====
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnFilter = new JButton("Filter");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnFilter);

        // Combine input + buttons in a top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== Table Panel =====
        model = new DefaultTableModel(new String[]{"ID", "Title", "Description", "Amount", "Date", "Category"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Button Actions =====
        btnAdd.addActionListener(e -> createExpense());
        btnUpdate.addActionListener(e -> updateExpense());
        btnDelete.addActionListener(e -> deleteExpense());
        btnRefresh.addActionListener(e -> loadExpenses());
        btnFilter.addActionListener(e -> filterExpenses());

        // ===== Table Row Click =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(model.getValueAt(row, 0).toString());
                    txtTitle.setText(model.getValueAt(row, 1).toString());
                    txtDesc.setText(model.getValueAt(row, 2).toString());
                    txtAmount.setText(model.getValueAt(row, 3).toString());
                    String catName = model.getValueAt(row, 5).toString();
                    cmbCategory.setSelectedItem(catName);
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
            int catId = categoryDAO.getCategoryIdByName(cmbCategory.getSelectedItem().toString());
            Expense e = new Expense(
                    0,
                    txtTitle.getText(),
                    txtDesc.getText(),
                    Double.parseDouble(txtAmount.getText()),
                    null,
                    catId
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
            int catId = categoryDAO.getCategoryIdByName(cmbCategory.getSelectedItem().toString());
            Expense e = new Expense(
                    Integer.parseInt(txtId.getText()),
                    txtTitle.getText(),
                    txtDesc.getText(),
                    Double.parseDouble(txtAmount.getText()),
                    null,
                    catId
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

    // ===== Load all expenses with category names =====
    private void loadExpenses() {
        try {
            model.setRowCount(0);
            List<Expense> list = expenseDAO.getAllExpenses();
            for (Expense e : list) {
                model.addRow(new Object[]{
                        e.getId(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getAmount(),
                        e.getDate(),
                        categoryDAO.getCategoryNameById(e.getCategoryId()) // FIXED
                });
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // ===== Filter expenses by category =====
    private void filterExpenses() {
        try {
            String[] categoryArray = categoryDAO.getAllCategoryNames().toArray(new String[0]);
            String category = (String) JOptionPane.showInputDialog(
                    this,
                    "Select Category to Filter:",
                    "Filter Expenses",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    categoryArray,
                    categoryArray.length > 0 ? categoryArray[0] : null
            );

            if (category != null) {
                int catId = categoryDAO.getCategoryIdByName(category);
                if (catId > 0) {
                    model.setRowCount(0);
                    List<Expense> list = expenseDAO.getExpensesByCategory(catId);
                    for (Expense e : list) {
                        model.addRow(new Object[]{
                                e.getId(),
                                e.getTitle(),
                                e.getDescription(),
                                e.getAmount(),
                                e.getDate(),
                                categoryDAO.getCategoryNameById(e.getCategoryId()) // FIXED
                        });
                    }
                }
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
