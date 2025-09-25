package com.exp.gui;  // ✅ Must match folder structure

import javax.swing.*; // other imports as needed

public class CategoryGUI extends JFrame {  // ✅ Class name must match file name
    public CategoryGUI() {
        super("Category GUI");
        // Your GUI code here
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
