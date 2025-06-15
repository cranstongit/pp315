package ru.kata.spring.boot_security.demo.exceptionhandler;

public class UserWrongData {
    String message;

    public UserWrongData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
