package com.exp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.exp.dao.CategoryDAO;
import com.exp.model.Category;

public class CategoryGUI extends JFrame {

    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton;
    private CategoryDAO dao = new CategoryDAO();

    public CategoryGUI() {
        super("Category Manager");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Category Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        categoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        // Double-click to update
        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    updateCategory();
                }
            }
        });

        // Load categories from DB
        loadCategories();

        setVisible(true);
    }

    // Load categories from DB
    private void loadCategories() {
        tableModel.setRowCount(0); // Clear existing rows
        try {
            List<Category> categories = dao.getAllCategories();
            for (Category cat : categories) {
                tableModel.addRow(new Object[]{cat.getId(), cat.getName()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add category
    private void addCategory() {
        String name = JOptionPane.showInputDialog(this, "Enter category name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                int id = dao.createCategory(new Category(name));
                tableModel.addRow(new Object[]{id, name});
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Update category
    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = tableModel.getValueAt(selectedRow, 1).toString();
        String newName = JOptionPane.showInputDialog(this, "Update category name:", currentName);
        if (newName != null && !newName.trim().isEmpty()) {
            try {
                Category cat = new Category(id, newName);
                if (dao.updateCategory(cat)) {
                    tableModel.setValueAt(newName, selectedRow, 1);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Delete category
    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                if (dao.deleteCategory(id)) {
                    tableModel.removeRow(selectedRow);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CategoryGUI::new);
    }
}
