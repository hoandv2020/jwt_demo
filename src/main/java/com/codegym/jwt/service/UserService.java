package com.codegym.jwt.service;

import com.codegym.jwt.model.User;

public interface UserService {
    User save(User user);
    User findByUsername(String username);
}
