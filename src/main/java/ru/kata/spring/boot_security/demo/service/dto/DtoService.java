package ru.kata.spring.boot_security.demo.service.dto;

import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.EditDto;
import ru.kata.spring.boot_security.demo.dto.NewDto;
import ru.kata.spring.boot_security.demo.dto.ResponseDto;
import ru.kata.spring.boot_security.demo.model.User;

public interface DtoService {
    ResponseDto buildResponseDto(User user);
    ResponseDto saveAndReturn(NewDto newDto);
    ResponseDto updateAndReturn(EditDto editDto);
    String bindingResultInfo(BindingResult bindingResult);
}