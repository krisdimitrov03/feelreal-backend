package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", unique = true)
    @NotNull
    @Length(min = 3, max = 30, message = "Username length must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Invalid username")
    private String username;

    @Column(name = "email", unique = true)
    @NotNull
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "first_name")
    @NotNull
    @Length(min = 3, max = 30, message = "First name length must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Invalid first name")
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Length(min = 3, max = 30, message = "Last name length must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Invalid last name")
    private String lastName;

    @Column(name = "password_hash")
    @NotNull
    private String passwordHash;

    @Column(name = "date_of_birth")
    @NotNull
    private LocalDate dateOfBirth;

    @Column(name = "role")
    @NotNull
    private Role role;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @NotNull
    private Job job;

    @Column(name = "created_at")
    @NotNull
    private LocalDate createdAt;

    @Column(name = "locked")
    @NotNull
    private boolean locked = false;

    @Column(name = "enabled")
    @NotNull
    private boolean enabled = true;

    public User(String username,
                String email,
                String firstName,
                String lastName,
                String passwordHash,
                LocalDate dateOfBirth,
                Role role,
                Job job,
                LocalDate createdAt) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.job = job;
        this.createdAt = createdAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());

        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
