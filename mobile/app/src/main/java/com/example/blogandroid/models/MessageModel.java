package com.example.blogandroid.models;

import java.util.Date;

public class MessageModel {
    private int id;
    private String username;
    private String content;
    private Date created;
    private int chat;
    private int user;

    public MessageModel(int id, String username, String content, Date created, int chat, int user) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.created = created;
        this.chat = chat;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getChat() {
        return chat;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
