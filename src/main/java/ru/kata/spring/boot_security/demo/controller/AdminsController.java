package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponceUserDto;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

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


//    @GetMapping("me1")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<User> getAdminInfo1(Principal principal) {
//
//        User user = userService.findByUsername(principal.getName());
//
//        if (user == null) {
//            throw new UserNotFoundException("User with the username " + principal.getName() + " not found in the DB");
//        }
//
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("me")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponceUserDto> getAdminInfo(Principal principal) {

        User user = userService.findByUsername(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("User with the username " + principal.getName() + " not found in the DB");
        }

        return ResponseEntity.ok(convertToResponceUserDto(user));
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





//    @GetMapping({""})
//    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
//    public ModelAndView adminPage(Principal principal) {
//
//        ModelAndView mavAdmin = new ModelAndView("admin");
//
//        mavAdmin.addObject("getUsers", userService.findAll()); //получаем всех пользователей
//        mavAdmin.addObject("admin", userService.findByUsername(principal.getName()));
//        mavAdmin.addObject("newUser", new User());
//        mavAdmin.addObject("allRoles", roleService.findAll()); // Добавим роли
//
//        return mavAdmin;
//    }


    @GetMapping("/error")
    public ModelAndView showError(@RequestParam(name = "errorMessage", required = false) String errorMessage) {

        ModelAndView mavError = new ModelAndView("error");

        if (errorMessage != null) {
            mavError.addObject("errorMessage", errorMessage);  // добавляем параметр в модель
        } else {
            mavError.addObject("errorMessage", "Something went wrong");
        }

        return mavError;
    }


    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView newUser(@ModelAttribute("newUser") User user) {

        try {
            userService.save(user);
        } catch (IllegalArgumentException e) {
            ModelAndView mavError = new ModelAndView("error"); // имя шаблона ошибки
            mavError.addObject("errorMessage", "Ошибка с ролями: " + e.getMessage());
            return mavError;
        } catch (Exception e) {
            ModelAndView mavError = new ModelAndView("error"); // имя шаблона ошибки
            mavError.addObject("errorMessage", "Ошибка: " + e.getMessage());
            return mavError;
        }

        return new ModelAndView("redirect:/admin");
    }
//
//
//    @PostMapping("/deleteuser")
//    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
//    public ModelAndView removeUser(@RequestParam("id") long id) {
//
//        try {
//            userService.delete(id);
//        } catch (EntityNotFoundException e) {
//            ModelAndView mavError = new ModelAndView("error");
//            mavError.addObject("errorMessage", "Проблема с удалением пользователя: " + e.getMessage());
//            return mavError;
//        }
//
//        return new ModelAndView("redirect:/admin");
//    }
//
//
//    @PostMapping("/edituser")
//    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
//    public ModelAndView updateUser(@RequestParam("id") long id, @ModelAttribute("user") User user) {
//
//        try {
//            userService.update(id, user);
//        } catch (EntityNotFoundException e) {
//            ModelAndView mavError = new ModelAndView("error");
//            mavError.addObject("errorMessage", "Ошибка при изменении: " + e.getMessage());
//            return mavError;
//        } catch (RuntimeException e) {
//            ModelAndView mavError = new ModelAndView("error");
//            mavError.addObject("errorMessage", "Ошибка при изменении: " + e.getMessage());
//            return mavError;
//        }
//
//        return new ModelAndView("redirect:/admin");
//    }

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

    public ResponceUserDto convertToResponceUserDto(User user) {
        ResponceUserDto responceUserDto = new ResponceUserDto();

        responceUserDto.setId(user.getId());
        responceUserDto.setFirstName(user.getFirstName());
        responceUserDto.setLastName(user.getLastName());
        responceUserDto.setEmail(user.getEmail());
        responceUserDto.setUsername(user.getUsername());
        responceUserDto.setRoles(user.getRoles());

        return responceUserDto;
    }

}
