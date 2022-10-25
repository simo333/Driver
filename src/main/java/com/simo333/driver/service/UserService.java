package com.simo333.driver.service;

import com.simo333.driver.model.User;

public interface UserService {
    User findByUserName(String name);

    void saveUser(User user);
}
