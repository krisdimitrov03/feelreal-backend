package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.*;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    RegisterResponse register(RegisterRequest data);

    Optional<String> login(LoginRequest data);

    Optional<User> getById(UUID id);

    OperationResult<UserProfile> getProfile(UUID id, UUID principalId);

    OperationResult<UpdateProfileResult> updateProfile(UUID id, UserUpdateRequest data, UUID principalId);

    OperationResult<UUID> deleteProfile(UUID id, UUID principalId);

}
