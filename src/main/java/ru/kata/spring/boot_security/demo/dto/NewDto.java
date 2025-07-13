package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

import lombok.Data;

@Data
public class NewDto {

    @NotEmpty(message = "Name should not be empty")
    private String firstName;

    private String lastName;

    @Email
    private String email;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "must be between 2 and 100")
    private String username;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "must be between 2 and 100")
    private String password;

    private List<Long> roleIds;
}