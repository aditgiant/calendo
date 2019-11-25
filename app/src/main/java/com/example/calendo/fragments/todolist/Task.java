package com.example.calendo.fragments.todolist;


public class Task {
    private String id;
    private String title;
    private String category;
    private String description;
    private String duedate;


    public Task(String id, String title, String category, String description, String duedate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.duedate = duedate;
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

    public String getDescription() {
        return description;
    }

    public String getDuedate() {
        return duedate;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }
}
