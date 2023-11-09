package com.example;

public class Query {

    private String number; // This corresponds to the <num> tag in the dataset
    private String title; // This corresponds to the <title> tag in the dataset
    private String description; // This corresponds to the <desc> tag in the dataset
    private String narrative; // This corresponds to the <narr> tag in the dataset

    // Constructor without parameters
    public Query() {
        // No fields to initialize, default constructor is sufficient
    }

    // Getters and setters for each property
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    // Override toString method if needed for debugging or logging purposes
    @Override
    public String toString() {
        return "Query{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", narrative='" + narrative + '\'' +
                '}';
    }
}
