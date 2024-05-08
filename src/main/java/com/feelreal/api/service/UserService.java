package com.feelreal.api.service;

import com.feelreal.api.dto.LoginDto;
import com.feelreal.api.dto.RegisterDto;
import com.feelreal.api.dto.RegisterResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    RegisterResponse register(RegisterDto data);

    Optional<String> login(LoginDto data);

    boolean logout(String token);

}
