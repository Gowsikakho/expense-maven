package com.exp;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.exp.gui.Home;
import com.exp.util.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        // Create database connection
        DatabaseConnection dbConnection = new DatabaseConnection();

        try {
            Connection cn = dbConnection.getDBConnection();
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            System.exit(1); // terminate process
        }

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Could not set Look and Feel: " + e.getMessage());
        }

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new Home().setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting the application: " + e.getLocalizedMessage());
            }
        });
    }
}