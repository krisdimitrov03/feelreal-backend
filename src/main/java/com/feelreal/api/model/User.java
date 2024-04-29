package com.feelreal.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

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

    @ManyToOne
    @JoinColumn(name = "job_id")
    @NotNull
    private Job job;

}
