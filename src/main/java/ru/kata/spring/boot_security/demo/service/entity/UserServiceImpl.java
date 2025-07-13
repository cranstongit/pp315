package ru.kata.spring.boot_security.demo.service.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.repository.entity.dao.UserDao;
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

   public UserServiceImpl(UserDao userDao) {
      this.userDao = userDao;
   }


   @Transactional
   @Override
   public void save(User user) {
      userDao.save(user);
   }


   @Transactional
   @Override
   public void update(long id, User user) {
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
   public Optional<User> find(long id) {
      return Optional.ofNullable(userDao.find(id));
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

}
