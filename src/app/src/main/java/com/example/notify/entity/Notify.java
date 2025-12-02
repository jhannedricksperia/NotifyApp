package com.example.notify.entity;

public class Notify {
    private int id;
    private String title, content, dateTimeCreated, dateTimeModified;

    public Notify(int id, String title, String content, String dateTimeCreated, String dateTimeModified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeModified = dateTimeModified;
    }


    public Notify(String title, String content, String dateTimeCreated, String dateTimeModified) {
        this.title = title;
        this.content = content;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeModified = dateTimeModified;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getDateTimeModified() {
        return dateTimeModified;
    }

    public void setDateTimeModified(String dateTimeModified) {
        this.dateTimeModified = dateTimeModified;
    }
}
