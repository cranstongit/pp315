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
import ru.kata.spring.boot_security.demo.dto.EditDto;
import ru.kata.spring.boot_security.demo.dto.NewDto;
import ru.kata.spring.boot_security.demo.dto.ResponseDto;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotFoundException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.dto.DtoService;
import ru.kata.spring.boot_security.demo.service.entity.RoleService;
import ru.kata.spring.boot_security.demo.service.entity.UserService;

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
    private final DtoService dtoService;

    public AdminsRestController(UserService userService, RoleService roleService, UserMapper userMapper, DtoService dtoService) {
        this.userService = userService;
        this.roleService = roleService;
        this.dtoService = dtoService;
    }


    @GetMapping({"users"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.findAll().orElse(Collections.emptyList()));
    }


    @GetMapping("me")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> getAdminInfo(Principal principal) {

        Optional<User> optionalUser = userService.findByUsername(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with the username " + principal.getName() + " not found in the DB");
        }

        return ResponseEntity.ok(dtoService.buildResponseDto(optionalUser.get()));
    }


    @PostMapping("newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody NewDto newDto,
                                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotCreatedException(dtoService.bindingResultInfo(bindingResult));
        }

        return ResponseEntity.ok().body(dtoService.saveAndReturn(newDto));
    }


    @PatchMapping("edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> updateUser(@Valid @RequestBody EditDto editDto,
                                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new UserNotUpdatedException(dtoService.bindingResultInfo(bindingResult));
        }

        return ResponseEntity.ok().body(dtoService.updateAndReturn(editDto));
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
