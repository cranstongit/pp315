package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.EditUserDto;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponseUserDto;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);
    void update(long id, User user);
    void delete(long id);
    Optional<List<User>> findAll();
    Optional<User> findByUsername(String username);
    User convertToNewUserDto(NewUserDto newUserDto);
    User convertToEditUserDto(EditUserDto editUserDto);
    ResponseUserDto convertToResponseUserDto(User user);
    String bindingResultInfo(BindingResult bindingResult);
}