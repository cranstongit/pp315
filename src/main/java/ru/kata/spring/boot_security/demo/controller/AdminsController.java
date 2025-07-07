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
import java.util.List;

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

        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("me")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseUserDto> getAdminInfo(Principal principal) {

        User user = userService.findByUsername(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("User with the username " + principal.getName() + " not found in the DB");
        }

        return ResponseEntity.ok(convertToResponseUserDto(user));
    }


    @PostMapping("newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody NewUserDto newUserDto,
                                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fe : fieldErrors) {
                sb.append(fe.getField())
                        .append(" - ").append(fe.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(sb.toString());
        }

        userService.save(convertToNewUserDto(newUserDto));

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
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fe : fieldErrors) {
                sb.append(fe.getField())
                        .append(" - ").append(fe.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotUpdatedException(sb.toString());
        }

        User user = convertToEditUserDto(editUserDto);

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


    //DTO methods
    public User convertToNewUserDto(NewUserDto newUserDto) {
        User user = new User();

        user.setUsername(newUserDto.getUsername());
        user.setFirstName(newUserDto.getFirstName());
        user.setLastName(newUserDto.getLastName());
        user.setEmail(newUserDto.getEmail());
        user.setPassword(newUserDto.getPassword());
        user.setRoleIds(newUserDto.getRoleIds());

        return user;
    }

    public User convertToEditUserDto(EditUserDto editUserDto) {
        User user = new User();

        user.setId(editUserDto.getId());
        user.setUsername(editUserDto.getUsername());
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setEmail(editUserDto.getEmail());
        user.setPassword(editUserDto.getPassword());
        user.setRoleIds(editUserDto.getRoleIds());

        return user;
    }

    public ResponseUserDto convertToResponseUserDto(User user) {
        ResponseUserDto responseUserDto = new ResponseUserDto();

        responseUserDto.setId(user.getId());
        responseUserDto.setFirstName(user.getFirstName());
        responseUserDto.setLastName(user.getLastName());
        responseUserDto.setEmail(user.getEmail());
        responseUserDto.setUsername(user.getUsername());
        responseUserDto.setRoles(user.getRoles());

        return responseUserDto;
    }

}
