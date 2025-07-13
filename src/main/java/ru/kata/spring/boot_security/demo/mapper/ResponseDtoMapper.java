package ru.kata.spring.boot_security.demo.mapper;

import ru.kata.spring.boot_security.demo.dto.ResponseDto;
import ru.kata.spring.boot_security.demo.model.User;

public interface ResponseDtoMapper {
    ResponseDto toEntity(User user);
}
