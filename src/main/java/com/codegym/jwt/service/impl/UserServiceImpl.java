package com.codegym.jwt.service.impl;

import com.codegym.jwt.model.User;
import com.codegym.jwt.repository.UserRepository;
import com.codegym.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
