package ru.kata.spring.boot_security.demo.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserWrongData> handleException(UserNotFoundException e) {
        return new ResponseEntity<>(new UserWrongData(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<UserWrongData> handleException(UserNotCreatedException e) {
        return new ResponseEntity<>(new UserWrongData(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotUpdatedException.class)
    public ResponseEntity<UserWrongData> handleException(UserNotUpdatedException e) {
        return new ResponseEntity<>(new UserWrongData(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserWrongData> handleException(Exception e) {
        return new ResponseEntity<>(new UserWrongData(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
