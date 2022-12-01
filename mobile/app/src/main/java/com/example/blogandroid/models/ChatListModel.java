package com.example.blogandroid.models;

import java.util.Date;

public class ChatListModel {
    private int id;
    private String user_one_name;
    private String user_two_name;
    private Date created;
    private int user_one;
    private int user_two;

    public ChatListModel(int id, String user_one_name, String user_two_name, Date created, int user_one, int user_two) {
        this.id = id;
        this.user_one_name = user_one_name;
        this.user_two_name = user_two_name;
        this.created = created;
        this.user_one = user_one;
        this.user_two = user_two;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_one_name() {
        return user_one_name;
    }

    public void setUser_one_name(String user_one_name) {
        this.user_one_name = user_one_name;
    }

    public String getUser_two_name() {
        return user_two_name;
    }

    public void setUser_two_name(String user_two_name) {
        this.user_two_name = user_two_name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getUser_one() {
        return user_one;
    }

    public void setUser_one(int user_one) {
        this.user_one = user_one;
    }

    public int getUser_two() {
        return user_two;
    }

    public void setUser_two(int user_two) {
        this.user_two = user_two;
    }
}
