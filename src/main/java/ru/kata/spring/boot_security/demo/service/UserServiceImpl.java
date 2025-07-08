package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.EditUserDto;
import ru.kata.spring.boot_security.demo.dto.NewUserDto;
import ru.kata.spring.boot_security.demo.dto.ResponseUserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

   private final UserDao userDao;
   private final RoleService roleService;
   private final PasswordEncoder passwordEncoder;

   public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
      this.userDao = userDao;
      this.roleService = roleService;
      this.passwordEncoder = passwordEncoder;
   }


   @Transactional
   @Override
   public void save(User user) {

      if (user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
         throw new IllegalArgumentException("Не выбрана ни одна роль.");
      }

      Set<Role> roles = roleService.findByIds(user.getRoleIds());

      if (roles == null || roles.isEmpty()) {
         throw new IllegalArgumentException("Указанные роли не найдены.");
      }

      user.setRoles(roles);
      user.setPassword(passwordEncoder.encode(user.getPassword()));

      userDao.save(user);
   }


   @Transactional
   @Override
   public void update(long id, User user) {

      User existingUser = userDao.find(id);

      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }

      if (user.getPassword() != null && !user.getPassword().isBlank()
              && !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
         user.setPassword(passwordEncoder.encode(user.getPassword()));
      } else {
         user.setPassword(existingUser.getPassword());
      }

      Set<Role> roles = roleService.findByIds(user.getRoleIds());
      user.setRoles(roles);
      user.setId(id);

      try {
         userDao.merge(user);
      } catch (RuntimeException e) {
         throw new RuntimeException("Merging error: " + e.getMessage());
      }
   }


   @Transactional
   @Override
   public void delete(long id) {

      User existingUser = userDao.find(id);

      if (existingUser == null) {
         throw new EntityNotFoundException("User with id " + id + " not found");
      }

      userDao.delete(id);
   }


   @Override
   public Optional<List<User>> findAll() {
      return Optional.ofNullable(userDao.findAll());
   }


   @Override
   public Optional<User> findByUsername(String username) {
      return Optional.ofNullable(userDao.findByUsername(username));
   }


   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User foundUser = userDao.findByUsername(username);

      if (foundUser == null)
         throw new UsernameNotFoundException(username + " not found");

      return new org.springframework.security.core.userdetails.User(foundUser.getUsername(),
                                                                     foundUser.getPassword(),
                                                                     mapRolesToAuthorities(foundUser.getRoles()));
   }

   private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
   }


   //DTO methods
   public User convertToNewUserDto(NewUserDto newUserDto) {
      User user = new User();

      user.setUsername(newUserDto.getUsername());
      user.setFirstName(newUserDto.getFirstName());
      user.setLastName(newUserDto.getLastName());
      user.setEmail(newUserDto.getEmail());
      user.setPassword(newUserDto.getPassword());
      user.setRoleIds(newUserDto.getRoleIds());

      return user;
   }

   public User convertToEditUserDto(EditUserDto editUserDto) {
      User user = new User();

      user.setId(editUserDto.getId());
      user.setUsername(editUserDto.getUsername());
      user.setFirstName(editUserDto.getFirstName());
      user.setLastName(editUserDto.getLastName());
      user.setEmail(editUserDto.getEmail());
      user.setPassword(editUserDto.getPassword());
      user.setRoleIds(editUserDto.getRoleIds());

      return user;
   }

   public ResponseUserDto convertToResponseUserDto(User user) {
      ResponseUserDto responseUserDto = new ResponseUserDto();

      responseUserDto.setId(user.getId());
      responseUserDto.setFirstName(user.getFirstName());
      responseUserDto.setLastName(user.getLastName());
      responseUserDto.setEmail(user.getEmail());
      responseUserDto.setUsername(user.getUsername());
      responseUserDto.setRoles(user.getRoles());

      return responseUserDto;
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
