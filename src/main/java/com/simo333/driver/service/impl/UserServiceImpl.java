package com.simo333.driver.service.impl;

import com.simo333.driver.model.Role;
import com.simo333.driver.model.User;
import com.simo333.driver.repository.UserRepository;
import com.simo333.driver.service.RoleService;
import com.simo333.driver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User findOne(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User with username '{}' not found", username);
            return new ResourceNotFoundException(String.format("User with username '%s' not found", username));
        });
    }

    @Override
    public User findOne(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id '{}' not found", userId);
            return new ResourceNotFoundException(String.format("User with id '%s' not found", userId));
        });
    }

    @Override
    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleService.findOne("ROLE_USER");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        findOne(user.getId());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
