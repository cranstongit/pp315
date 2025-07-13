package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import lombok.Data;

@Data
public class EditDto {

    @NotNull(message = "id should not be null")
    @Min(value = 1, message = "id must be greater than 0")
    private Long id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String username;

    private String password;

    @NotNull(message = "roleIds should not be null")
    @Size(min = 1, message = "At least one role must be selected")
    private List<Long> roleIds;
}