package com.example.company.myplanner.utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Mohamed Sayed on 11/9/2017.
 */

public class Budget implements Serializable {
    private String commitment;
    private String value;
    private String key;
    private String userId;

    public String getCommitment() {
        return commitment;
    }

    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, String> toFirebaseObject() {
        HashMap<String, String> budget = new HashMap<String, String>();
        budget.put("commitment", commitment);
        budget.put("value", value);
        budget.put("userId", userId);
        return budget;
    }

}
