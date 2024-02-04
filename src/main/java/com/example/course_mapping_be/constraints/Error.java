package com.example.course_mapping_be.constraints;

public enum Error {
    INVALID_AUTHORIZATION("Invalid authorization"),
    EMAIL_EXISTED("Email existed"),
    INVALID_EMAIL("Invalid email"),
    INVALID_PASSWORD("Invalid password");


    private final String message;

    Error(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }
}
