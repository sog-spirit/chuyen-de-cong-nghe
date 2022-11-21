package com.example.blogandroid.models;

import java.util.Date;

public class CommentModel {
    private int id;
    private String name;
    private int post;
    private int user;
    private String content;
    private Date created_on;
    private Date updated_on;

    public CommentModel(int id, String name, int post, int user, String content, Date created_on, Date updated_on) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.user = user;
        this.content = content;
        this.created_on = created_on;
        this.updated_on = updated_on;
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

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
