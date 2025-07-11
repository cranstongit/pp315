package ru.kata.spring.boot_security.demo.mapper;

import ru.kata.spring.boot_security.demo.dto.EditUserDto;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponseUserDto;
import ru.kata.spring.boot_security.demo.model.User;

public interface UserMapper {
    User toEntity(NewUserDto newUserDto);
    ResponseUserDto saveAndReturn(NewUserDto newUserDto);
    User toEntity(EditUserDto editUserDto);
    ResponseUserDto saveAndReturn(EditUserDto editUserDto);
    ResponseUserDto toEntity(User user);
}
