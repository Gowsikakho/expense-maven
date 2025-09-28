package com.exp.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Home extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public Home() {
        super("Expense Tracker");

        // ===== CardLayout for whole frame =====
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // ===== Home Panel (original design) =====
        JPanel homePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        homePanel.add(welcomeLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton categoriesButton = new JButton("Categories");
        JButton expensesButton = new JButton("Expenses");

        categoriesButton.setPreferredSize(new Dimension(200, 80));
        expensesButton.setPreferredSize(new Dimension(200, 80));
        categoriesButton.setFont(new Font("Arial", Font.BOLD, 24));
        expensesButton.setFont(new Font("Arial", Font.BOLD, 24));

        buttonPanel.add(categoriesButton);
        buttonPanel.add(expensesButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        homePanel.add(buttonPanel, gbc);

        // ===== Category Panel with Back =====
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.add(new CategoryGUI(), BorderLayout.CENTER);
        JButton backFromCategory = new JButton("Back");
        backFromCategory.addActionListener(e -> cardLayout.show(cardPanel, "Home"));
        JPanel catBottom = new JPanel();
        catBottom.add(backFromCategory);
        categoryPanel.add(catBottom, BorderLayout.SOUTH);

        // ===== Expense Panel with Back =====
        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(new ExpenseGUI(), BorderLayout.CENTER);
        JButton backFromExpense = new JButton("Back");
        backFromExpense.addActionListener(e -> cardLayout.show(cardPanel, "Home"));
        JPanel expBottom = new JPanel();
        expBottom.add(backFromExpense);
        expensePanel.add(expBottom, BorderLayout.SOUTH);

        // ===== Add all panels to cardPanel =====
        cardPanel.add(homePanel, "Home");
        cardPanel.add(categoryPanel, "Categories");
        cardPanel.add(expensePanel, "Expenses");

        // ===== Button actions =====
        categoriesButton.addActionListener(e -> cardLayout.show(cardPanel, "Categories"));
        expensesButton.addActionListener(e -> cardLayout.show(cardPanel, "Expenses"));

        // ===== Frame settings =====
        add(cardPanel);
        cardLayout.show(cardPanel, "Home");

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Home::new);
    }
}
