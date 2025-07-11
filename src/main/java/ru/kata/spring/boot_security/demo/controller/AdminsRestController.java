package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.EditUserDto;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponseUserDto;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminsRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public AdminsRestController(UserService userService, RoleService roleService, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }


    @GetMapping({"users"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUserList() {
        List<User> users = userService.findAll().orElse(Collections.emptyList());

        return ResponseEntity.ok(users);
    }


    @GetMapping("me")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseUserDto> getAdminInfo(Principal principal) {

        Optional<User> user = userService.findByUsername(principal.getName());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the username " + principal.getName() + " not found in the DB");
        }

        return ResponseEntity.ok(userMapper.toEntity(user.get()));
    }


    @PostMapping("newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseUserDto> createUser(@Valid @RequestBody NewUserDto newUserDto,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(userService.bindingResultInfo(bindingResult));
        }

        return ResponseEntity.ok().body(userMapper.saveAndReturn(newUserDto));
    }


    @PatchMapping("edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseUserDto> updateUser(@Valid @RequestBody EditUserDto editUserDto,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(userService.bindingResultInfo(bindingResult));
        }

        return ResponseEntity.ok().body(userMapper.saveAndReturn(editUserDto));
    }


    @DeleteMapping("delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public ResponseEntity<String> removeUser(@RequestParam long id) {

        try {
            userService.delete(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь c " + id + " не найден: " + e.getMessage());
        }
    }


    @GetMapping("roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {

        return ResponseEntity.ok(new ArrayList<>(roleService.findAll()));
    }

}
