package com.exp.model;

public class Expense {
    private int id;
    private String title;
    private String description;
    private double amount;
    private String date;
    private int categoryId;

    public Expense() {
        this.date = java.time.LocalDate.now().toString();
    }

    public Expense(String title, double amount, int categoryId) {
        this();
        this.title = title;
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public Expense(int id, String title, String description, double amount, String date, int categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    @Override
    public String toString() {
        return "Expense [id=" + id + ", title=" + title + ", description=" + description +
               ", amount=" + amount + ", date=" + date + ", categoryId=" + categoryId + "]";
    }
}
