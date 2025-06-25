package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class UserDto {

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
