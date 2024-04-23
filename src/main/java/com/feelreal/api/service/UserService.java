package com.feelreal.api.service;

import com.feelreal.api.model.User;
import com.feelreal.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository) {
    }

    public User addUser(User user) {
        return new User();
    }

    public User getUser(Integer id) {
        return null;
    }

    public User updateUser(User user) {
        return null;
    }

    public void deleteUser(Integer id) {
    }

}
