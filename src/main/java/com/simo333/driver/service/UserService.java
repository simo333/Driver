package com.simo333.driver.service;

import com.simo333.driver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Page<User> findAll(Pageable page);

    User findOne(Long userId);

    User findOne(String username);

    boolean existsByUsername(String username);

    User save(User user);

    User update(User user);

    void delete(Long id);
}
