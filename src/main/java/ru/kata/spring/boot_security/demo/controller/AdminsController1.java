//package ru.kata.spring.boot_security.demo.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
//import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotFoundException;
//import ru.kata.spring.boot_security.demo.model.User;
//import ru.kata.spring.boot_security.demo.service.RoleService;
//import ru.kata.spring.boot_security.demo.service.UserService;
//
//import javax.persistence.EntityNotFoundException;
//import javax.validation.Valid;
//import java.security.Principal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//public class AdminsController1 {
//
//    private final UserService userService;
//    private final RoleService roleService;
//
//    public AdminsController1(UserService userService, RoleService roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
//    }
//
//
//    @GetMapping({"users"}) //какой адрес использовать, должен быть admin?
//    public List<User> getUserList() {
//
//        return userService.findAll();
//    }
//
//
//    @GetMapping("users")
//    public User userPage(@RequestParam("username") String username) {
//
//        if (userService.findByUsername(username) == null) {
//            throw new UserNotFoundException("User with the username " + username + " not found in the DB");
//        }
//
//        return userService.findByUsername(username);
//    }
//
//
//    @PostMapping
//    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody User user,
//                                                 BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            StringBuilder sb = new StringBuilder();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fe : fieldErrors) {
//                sb.append(fe.getField())
//                        .append(" - ").append(fe.getDefaultMessage())
//                        .append(";");
//            }
//            throw new UserNotCreatedException(sb.toString());
//        }
//
//        userService.save(user);
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//}