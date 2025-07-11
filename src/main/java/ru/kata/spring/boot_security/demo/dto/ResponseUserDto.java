package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public record ResponseUserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        Set<Role> roles
        ) {
}