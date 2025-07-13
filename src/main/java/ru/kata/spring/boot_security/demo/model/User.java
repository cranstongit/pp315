package ru.kata.spring.boot_security.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "first_name", nullable = false, length = 100)
   private String firstName;

   @Column(name = "last_name", nullable = false, length = 100)
   private String lastName;

   @Column(name = "email", nullable = false, length = 100)
   @Email
   private String email;

   @Column(name = "username", nullable = false, unique = true, length = 100)
   @NotEmpty(message = "Name should not be empty")
   @Size(min = 2, max = 100, message = "must be between 2 and 100")
   private String username;

   @Column(name = "password", nullable = false, length = 100)
   @NotEmpty(message = "Name should not be empty")
   @Size(min = 2, max = 100, message = "must be between 2 and 100")
   private String password;

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )

   private Set<Role> roles;


   public User() {}


    @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }


   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return roles;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return Objects.equals(id, user.id);
   }

   @Override
   public int hashCode() {
      return 31 * Objects.hash(id);
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder("User - ");
      sb.append("id: ").append(id).append("\n");
      sb.append("firstName: ").append(firstName).append("\n");
      sb.append("lastName: ").append(lastName).append("\n");
      sb.append("email: ").append(email).append("\n");
      sb.append("username: ").append(username).append("\n");
      return sb.toString();
   }

}