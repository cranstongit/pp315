package ru.kata.spring.boot_security.demo.exceptionhandler;

public class UserNotUpdatedException extends RuntimeException{
    public UserNotUpdatedException(String message) {
        super(message);
    }
}
