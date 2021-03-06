package com.example.company.myplanner.utils;

/**
 * Created by Mohamed Sayed on 10/30/2017.
 */

public class User {
    private String name, email, password, dob, gender, type, salary, balance;

    public User() {
    }

    public User(String name, String email, String password, String dob, String gender, String type, String salary, String balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.type = type;
        this.salary = salary;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
