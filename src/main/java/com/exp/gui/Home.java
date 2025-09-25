package com.exp.gui;

import javax.swing.*;
import java.awt.*;
import com.exp.gui.CategoryGUI;
import com.exp.gui.ExpenseGUI;

public class Home extends JFrame {

    public Home() {
        super("Expense Tracker");

        // Use GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20); // spacing around components

        // Welcome label at top
        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // span two columns
        add(welcomeLabel, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton categoriesButton = new JButton("Categories");
        JButton expensesButton = new JButton("Expenses");

        // Button size and font
        categoriesButton.setPreferredSize(new Dimension(200, 80));
        expensesButton.setPreferredSize(new Dimension(200, 80));
        categoriesButton.setFont(new Font("Arial", Font.BOLD, 24));
        expensesButton.setFont(new Font("Arial", Font.BOLD, 24));

        buttonPanel.add(categoriesButton);
        buttonPanel.add(expensesButton);

        // Add button panel below label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Button actions
        categoriesButton.addActionListener(e -> new CategoryGUI().setVisible(true));
        expensesButton.addActionListener(e -> new ExpenseGUI().setVisible(true));

        // Frame settings
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
