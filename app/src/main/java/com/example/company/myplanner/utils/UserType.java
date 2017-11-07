package com.example.company.myplanner.utils;

/**
 * Created by Mohamed Sayed on 10/30/2017.
 */

public enum UserType {
    STUDENT("Student"), EMPLOYEE("Employee");
    private final String name;

    private UserType(String name) {
        this.name = name;
    }

    /**
     * @return The string representation of this element in the enumeration.
     */
    public String getName() {
        return this.name;
    }

}
