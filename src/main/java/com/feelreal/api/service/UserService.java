package com.feelreal.api.service;

import com.feelreal.api.model.dto.RegisterDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean register(RegisterDto data);


}
