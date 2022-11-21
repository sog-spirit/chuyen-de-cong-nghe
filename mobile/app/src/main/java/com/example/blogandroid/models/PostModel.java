package com.example.blogandroid.models;

import java.util.Date;

public class PostModel {
    private int id;
    private String name;
    private String title;
    private int user;
    private Date created_on;
    private Date updated_on;
    private String content;
    private String status;

    public PostModel(int id, String name, String title, int user, Date created_on, Date updated_on, String content, String status) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.user = user;
        this.created_on = created_on;
        this.updated_on = updated_on;
        this.content = content;
        this.status = status;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public Date getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Date updated_on) {
        this.updated_on = updated_on;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
