package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.entity.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ModelAndView visitUserPage(Principal principal) {

        if (principal == null || principal.getName() == null) {
            return new ModelAndView("error", "errorMessage", "Проблема с аутентификацией.");
        }

        Optional<User> user = userService.findByUsername(principal.getName());
        return user.map(value -> new ModelAndView("user", "user", value))
                .orElseGet(() -> new ModelAndView("error", "errorMessage", "Данные пользователя отсутствуют в БД."));
    }


    @GetMapping("error")
    public ModelAndView showError(@RequestParam(name = "errorMessage", required = false) String errorMessage) {
        return new ModelAndView("error")
                .addObject("errorMessage", Objects
                        .requireNonNullElse(errorMessage, "Something went wrong"));  // добавляем параметр в модель
    }

}