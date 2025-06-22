package ru.kata.spring.boot_security.demo.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserWrongData> handleException(UserNotFoundException e) {
        UserWrongData uwd = new UserWrongData(e.getMessage());
        return new ResponseEntity<>(uwd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserWrongData> handleException(UserNotCreatedException e) {
        UserWrongData uwd = new UserWrongData(e.getMessage());
        return new ResponseEntity<>(uwd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserWrongData> handleException(Exception e) {
        UserWrongData uwd = new UserWrongData(e.getMessage());
        return new ResponseEntity<>(uwd, HttpStatus.BAD_REQUEST);
    }
}
