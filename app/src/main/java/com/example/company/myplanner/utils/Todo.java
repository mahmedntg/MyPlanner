package com.example.company.myplanner.utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Mohamed Sayed on 11/4/2017.
 */

public class Todo implements Serializable {
    private String name;
    private String message;
    private String date;
    private String userId;
    private String key;

    public Todo() {

    }

    public Todo(String name, String message, String date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, String> toFirebaseObject() {
        HashMap<String, String> todo = new HashMap<String, String>();
        todo.put("name", name);
        todo.put("message", message);
        todo.put("date", date);
        todo.put("userId", userId);
        return todo;
    }
}
