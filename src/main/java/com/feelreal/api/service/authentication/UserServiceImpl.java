package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.*;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.dto.common.ResultStatus;
import com.feelreal.api.model.Job;
import com.feelreal.api.model.User;
import com.feelreal.api.model.WellnessCheck;
import com.feelreal.api.model.enumeration.Role;
import com.feelreal.api.repository.UserRepository;
import com.feelreal.api.repository.WellnessCheckRepository;
import com.feelreal.api.service.job.JobService;
import com.feelreal.api.service.wellnessChecks.WellnessCheckService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final JwtService jwtService;
    private final JobService jobService;
    private final WellnessCheckRepository wellnessCheckRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository repo,
            JwtService jwtService,
            JobService jobService,
            WellnessCheckRepository wellnessCheckRepository,
            PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.jobService = jobService;
        this.wellnessCheckRepository = wellnessCheckRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    @Override
    public Optional<User> getById(UUID id) {
        return repo.findById(id);
    }

    @Transactional
    @Override
    public OperationResult<UserProfile> getProfile(UUID id, UUID principalId) {
        Optional<User> user = getById(id);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        UserProfile profile = new UserProfile(
                user.get().getId(),
                user.get().getUsername(),
                null,
                user.get().getFirstName(),
                user.get().getLastName(),
                null,
                user.get().getJob()
        );

        if (user.get().getId().equals(principalId)) {
            profile.setEmail(user.get().getEmail());
            profile.setDateOfBirth(user.get().getDateOfBirth().toString());
        }

        return new OperationResult<>(ResultStatus.SUCCESS, profile);
    }

    @Transactional
    @Override
    public OperationResult<UpdateProfileResult> updateProfile(UUID id, UserUpdateRequest data, UUID principalId) {
        Optional<User> user = getById(id);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!user.get().getId().equals(principalId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        Optional<Job> job = data.getJobId() == null
                ? Optional.empty()
                : jobService.getById(UUID.fromString(data.getJobId()));

        if (job.isEmpty()) {
            return new OperationResult<>(ResultStatus.INVALID_INPUT, null);
        }

        User updatedUser = updateUserEntity(data, user.get(), job.get());

        String token = jwtService.generateToken(new TokenData(
                updatedUser.getId().toString(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getRole().getValue())
        );

        repo.saveAndFlush(updatedUser);

        return new OperationResult<>(ResultStatus.SUCCESS, new UpdateProfileResult(updatedUser.getId(), token));
    }

    @Transactional
    @Override
    public OperationResult<UUID> deleteProfile(UUID id, UUID principalId) {
        Optional<User> user = getById(id);

        if (user.isEmpty()) {
            return new OperationResult<>(ResultStatus.DOES_NOT_EXIST, null);
        }

        if (!user.get().getId().equals(principalId)) {
            return new OperationResult<>(ResultStatus.NO_PERMISSION, null);
        }

        wellnessCheckRepository.deleteByUserId(id);
        wellnessCheckRepository.flush();

        repo.delete(user.get());
        repo.flush();

        return new OperationResult<>(ResultStatus.SUCCESS, user.get().getId());
    }

    @Transactional
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

    private User updateUserEntity(UserUpdateRequest data, User user, Job job) {
        if (data.getUsername() != null) {
            user.setUsername(data.getUsername());
        }

        if (data.getEmail() != null) {
            user.setEmail(data.getEmail());
        }

        if (data.getFirstName() != null) {
            user.setFirstName(data.getFirstName());
        }

        if (data.getLastName() != null) {
            user.setLastName(data.getLastName());
        }

        if (data.getDateOfBirth() != null) {
            user.setDateOfBirth(LocalDate.parse(data.getDateOfBirth()));
        }

        user.setJob(job);

        return user;
    }

}