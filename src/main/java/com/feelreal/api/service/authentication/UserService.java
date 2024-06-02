package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.*;
import com.feelreal.api.dto.common.OperationResult;
import com.feelreal.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService extends UserDetailsService {

    RegisterResponse register(RegisterRequest data);

    Optional<String> login(LoginRequest data);

    Optional<User> getById(UUID id);

    OperationResult<UserProfile> getProfile(UUID id, UUID principalId);

    OperationResult<UUID> updateProfile(UUID id, UserUpdateRequest data, UUID principalId);

    OperationResult<UUID> deleteProfile(UUID id, UUID principalId);

}
