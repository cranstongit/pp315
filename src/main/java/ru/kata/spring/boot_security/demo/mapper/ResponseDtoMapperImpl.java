package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.ResponseDto;
import ru.kata.spring.boot_security.demo.model.User;

@Component
public class ResponseDtoMapperImpl implements ResponseDtoMapper {

    @Override
    public ResponseDto toEntity(User user) {
        return new ResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles()
        );
    }
}