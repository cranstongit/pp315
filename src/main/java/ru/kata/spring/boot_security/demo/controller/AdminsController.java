package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

        return ResponseEntity.ok(userService.convertToResponseUserDto(user.get()));
    }


    @PostMapping("newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody NewUserDto newUserDto,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(userService.bindingResultInfo(bindingResult));
        }

        userService.save(userService.convertToNewUserDto(newUserDto));

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping("roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {

        return ResponseEntity.ok(new ArrayList<>(roleService.findAll()));
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


    @PatchMapping("edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateUser(@Valid @RequestBody EditUserDto editUserDto,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(userService.bindingResultInfo(bindingResult));
        }

        User user = userService.convertToEditUserDto(editUserDto);

        try {
            userService.update(user.getId(), user);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь c " + editUserDto.getId() + " не найден: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка изменения пользователя с " + editUserDto.getId() + ". Ошибка: " + e.getMessage());
        }
    }


//    public ResponseEntity<User(можно поменят на дто)> updateUser(@RequestBody User user) {
//        if (!userService.isUserExist(user.getId())){
//            return ResponseEntity.notFound().build();
//        }
//        userService.updateUser(user);
//        return ResponseEntity.ok().body(user);
//    }

}
