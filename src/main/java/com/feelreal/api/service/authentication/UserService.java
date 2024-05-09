package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.LoginRequest;
import com.feelreal.api.dto.authentication.RegisterRequest;
import com.feelreal.api.dto.authentication.RegisterResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    RegisterResponse register(RegisterRequest data);

    Optional<String> login(LoginRequest data);

}
