package com.example.calendo.fragments.todolist;


public class Task {
    private String id;
    private String title;
    private String category;
    private String notes;
    private String date;
    private String status;


    public Task(String id, String title, String category, String notes, String date, String status) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.notes = notes;
        this.date = date;
        this.status = status;
    }

    public Task(){
        //public no arg constructor needed
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
