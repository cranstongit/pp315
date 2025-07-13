package ru.kata.spring.boot_security.demo.mapper;

import ru.kata.spring.boot_security.demo.dto.EditDto;
import ru.kata.spring.boot_security.demo.dto.NewDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

public interface UserMapper {
    User toEntity(NewDto newDto, Set<Role> roles);
    User toEntity(EditDto editDto, User existingUser, Set<Role> roles);
}
