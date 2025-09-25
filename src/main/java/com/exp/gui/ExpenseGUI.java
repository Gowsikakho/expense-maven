package com.exp.gui;  // ✅ Must match folder structure

import javax.swing.*; // other imports as needed

public class ExpenseGUI extends JFrame {  // ✅ Class name must match file name
    public ExpenseGUI() {
        super("Expense GUI");
        // Your GUI code here
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
