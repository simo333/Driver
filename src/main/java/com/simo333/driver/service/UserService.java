package com.simo333.driver.service;

import com.simo333.driver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> findAll(Pageable page);

    User findOne(Long userId);

    User findOne(String username);

    User save(User user);

    User update(User user);

    void delete(Long id);
}
