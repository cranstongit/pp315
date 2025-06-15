package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login1"; // имя шаблона login1.html
//    }

}