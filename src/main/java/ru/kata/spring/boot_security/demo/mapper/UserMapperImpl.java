package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.EditDto;
import ru.kata.spring.boot_security.demo.dto.NewDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapperImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User toEntity(NewDto newDto, Set<Role> roles) {
        User user = new User();
        user.setUsername(newDto.getUsername());
        user.setFirstName(newDto.getFirstName());
        user.setLastName(newDto.getLastName());
        user.setEmail(newDto.getEmail());
        user.setPassword(passwordEncoder.encode(newDto.getPassword()));
        user.setRoles(roles);
        return user;
    }


    @Override
    public User toEntity(EditDto editDto, User existingUser, Set<Role> roles) {
        existingUser.setFirstName(editDto.getFirstName());
        existingUser.setLastName(editDto.getLastName());
        existingUser.setEmail(editDto.getEmail());
        existingUser.setUsername(editDto.getUsername());
        existingUser.setRoles(roles);
        if (editDto.getPassword() != null && !editDto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(editDto.getPassword()));
        }
        return existingUser;
    }

}
