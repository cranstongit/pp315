package ru.kata.spring.boot_security.demo.service.dto;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.dto.EditDto;
import ru.kata.spring.boot_security.demo.dto.NewDto;
import ru.kata.spring.boot_security.demo.dto.ResponseDto;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.exceptionhandler.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.mapper.ResponseDtoMapper;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.entity.RoleService;
import ru.kata.spring.boot_security.demo.service.entity.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DtoServiceImpl implements DtoService {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final ResponseDtoMapper responseDtoMapper;

    public DtoServiceImpl(UserService userService, RoleService roleService,
                          UserMapper userMapper, ResponseDtoMapper responseDtoMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.responseDtoMapper = responseDtoMapper;
    }


    @Override
    public ResponseDto buildResponseDto(User user) {
        return responseDtoMapper.toEntity(user);
    }


    @Override
    public ResponseDto saveAndReturn(NewDto newDto) {
        if (newDto.getRoleIds() == null || newDto.getRoleIds().isEmpty()) {
            throw new UserNotCreatedException("Не выбрана ни одна роль.");
        }

        Set<Role> roles = roleService.findByIds(newDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new UserNotCreatedException("Указанные роли не найдены.");
        }

        User user = userMapper.toEntity(newDto, roles);
        userService.save(user);
        return buildResponseDto(user);
    }


    @Override
    public ResponseDto updateAndReturn(EditDto editDto) {
        if (editDto.getRoleIds() == null || editDto.getRoleIds().isEmpty()) {
            throw new UserNotUpdatedException("Не выбрана ни одна роль.");
        }

        Set<Role> roles = roleService.findByIds(editDto.getRoleIds());
        if (roles.isEmpty()) {
            throw new UserNotUpdatedException("Указанные роли не найдены.");
        }

        Optional<User> optionalUser = userService.find(editDto.getId());
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with id " + editDto.getId() + " not found");
        }

        User user = userMapper.toEntity(editDto, optionalUser.get(), roles);
        userService.update(user.getId(), user);
        return buildResponseDto(user);
    }


    public String bindingResultInfo(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fe : fieldErrors) {
            sb.append(fe.getField())
                    .append(" - ").append(fe.getDefaultMessage())
                    .append(";");
        }
        return sb.toString();
    }

}