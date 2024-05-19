package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.LoginRequest;
import com.feelreal.api.dto.authentication.RegisterRequest;
import com.feelreal.api.dto.authentication.RegisterResponse;
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

}
