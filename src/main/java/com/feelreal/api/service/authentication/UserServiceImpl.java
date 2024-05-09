package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.LoginRequest;
import com.feelreal.api.dto.authentication.RegisterResponse;
import com.feelreal.api.dto.authentication.TokenData;
import com.feelreal.api.model.Job;
import com.feelreal.api.model.User;
import com.feelreal.api.dto.authentication.RegisterRequest;
import com.feelreal.api.model.enumeration.Role;
import com.feelreal.api.repository.UserRepository;
import com.feelreal.api.service.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final JwtService jwtService;
    private final JobService jobService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository repo,
            JwtService jwtService,
            JobService jobService,
            PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.jobService = jobService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest data) {
        boolean userExists = repo
                .findByUsername(data.getUsername())
                .isPresent();

        if (userExists) {
            return new RegisterResponse(false, List.of("User with this username already exists"));
        }

        Optional<Job> jobOpt = jobService.getById(data.getJobId());

        if (jobOpt.isEmpty()) {
            return new RegisterResponse(false, List.of("Invalid job id"));
        }

        try {
            User user = createUserEntity(data, jobOpt.get());
            repo.saveAndFlush(user);
        } catch (Exception e) {
            return new RegisterResponse(false, List.of("Registration not successful"));
        }

        return new RegisterResponse(true, Collections.emptyList());
    }

    @Override
    public Optional<String> login(LoginRequest data) {
        Optional<User> userOpt = repo.findByUsername(data.getUsername());

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        if (!passwordEncoder.matches(data.getPassword(), userOpt.get().getPassword())) {
            return Optional.empty();
        }

        TokenData tokenData = new TokenData(
                userOpt.get().getId().toString(),
                userOpt.get().getUsername(),
                userOpt.get().getEmail(),
                userOpt.get().getRole().getValue());

        String token = jwtService.generateToken(tokenData);

        return Optional.of(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User createUserEntity(RegisterRequest data, Job job) {
        return new User(
                data.getUsername(),
                data.getEmail(),
                data.getFirstName(),
                data.getLastName(),
                passwordEncoder.encode(data.getPassword()),
                LocalDate.parse(data.getDateOfBirth()),
                Role.USER,
                job,
                LocalDate.now()
        );
    }
}