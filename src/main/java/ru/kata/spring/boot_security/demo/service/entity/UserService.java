package ru.kata.spring.boot_security.demo.service.entity;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);
    void update(long id, User user);
    void delete(long id);
    Optional<List<User>> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> find(long id);
}