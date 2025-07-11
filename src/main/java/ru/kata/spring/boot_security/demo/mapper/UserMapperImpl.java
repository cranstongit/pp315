package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.EditUserDto;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponseUserDto;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Component
public class UserMapperImpl implements UserMapper {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public UserMapperImpl(UserService userService, RoleService roleService,
                          PasswordEncoder passwordEncoder, UserDao userDao) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public User toEntity(NewUserDto newUserDto) {
        if (newUserDto.getRoleIds() == null || newUserDto.getRoleIds().isEmpty()) {
            throw new UserNotCreatedException("Не выбрана ни одна роль.");
        }

        Set<Role> roles = roleService.findByIds(newUserDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new UserNotCreatedException("Указанные роли не найдены.");
        }

        User user = new User();
        user.setUsername(newUserDto.getUsername());
        user.setFirstName(newUserDto.getFirstName());
        user.setLastName(newUserDto.getLastName());
        user.setEmail(newUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setRoles(roles);

        return user;
    }

    @Override
    public ResponseUserDto saveAndReturn(NewUserDto newUserDto) {
        User user = toEntity(newUserDto);
        userService.save(user);
        return toEntity(user);
    }


    @Override
    public User toEntity(EditUserDto editUserDto) {
        User existingUser = userDao.find(editUserDto.getId());
        if (existingUser == null) {
            throw new EntityNotFoundException("User with id " + existingUser.getId() + " not found");
        }

        if (editUserDto.getRoleIds() == null || editUserDto.getRoleIds().isEmpty()) {
            throw new UserNotUpdatedException("Не выбрана ни одна роль.");
        }

        Set<Role> roles = roleService.findByIds(editUserDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new UserNotUpdatedException("Указанные роли не найдены.");
        }

        existingUser.setFirstName(editUserDto.getFirstName());
        existingUser.setLastName(editUserDto.getLastName());
        existingUser.setEmail(editUserDto.getEmail());
        existingUser.setUsername(editUserDto.getUsername());
        existingUser.setRoles(roles);
        if (editUserDto.getPassword() != null && !editUserDto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }

        return existingUser;
    }

    @Override
    public ResponseUserDto saveAndReturn(EditUserDto editUserDto) {
        User user = toEntity(editUserDto);
        userService.update(user.getId(), user);
        return toEntity(user);
    }


    @Override
    public ResponseUserDto toEntity(User user) {
        return new ResponseUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles()
        );
    }
}
