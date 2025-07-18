package ru.kata.spring.boot_security.demo.service.entity;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Set<Role> findAll();
    Set<Role> findByIds(List<Long> ids);
}