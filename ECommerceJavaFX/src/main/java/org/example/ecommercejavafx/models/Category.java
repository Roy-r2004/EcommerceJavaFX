package org.example.ecommercejavafx.models;

public class Category {
    private int id;
    private String name;
    private String description;

    // Constructors
    public Category() {}

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Override toString method to improve ComboBox display
    @Override
    public String toString() {
        return name;  // This ensures the ComboBox shows the category name instead of object reference
    }
}
