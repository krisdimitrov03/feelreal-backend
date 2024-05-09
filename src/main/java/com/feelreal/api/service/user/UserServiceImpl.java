package com.feelreal.api.service.user;

import com.feelreal.api.model.User;
import com.feelreal.api.dto.RegisterDto;
import com.feelreal.api.repository.UserRepository;
import com.feelreal.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Autowired
    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean register(RegisterDto data) {
        User user = new User();
        user.setUsername(data.username());
        user.setEmail(data.email());
        user.setPasswordHash("daswdfhjiosuahfjiskajlhfrasoigfsa");

        repo.saveAndFlush(user);

        return true;
    }
}
