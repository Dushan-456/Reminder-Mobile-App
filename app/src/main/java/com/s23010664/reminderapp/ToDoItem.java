package com.s23010664.reminderapp;

public class ToDoItem {
    private int id;
    private String title;
    private String date;
    private String time;
    private String location;

    // Constructor, getters, and setters
    public ToDoItem() {}

    public ToDoItem(int id, String title, String date, String time, String location) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setLocation(String location) { this.location = location; }
}
