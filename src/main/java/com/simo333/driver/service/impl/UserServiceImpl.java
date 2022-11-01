package com.simo333.driver.service.impl;

import com.simo333.driver.model.User;
import com.simo333.driver.repository.UserRepository;
import com.simo333.driver.service.RefreshTokenService;
import com.simo333.driver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService tokenService;

    @Override
    public Page<User> findAll(Pageable page) {
        return userRepository.findAll(page);
    }

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findOne(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        log.info("Saving a new user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        findOne(user.getId());
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        log.info("Updating user with id '{}'", user.getId());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        tokenService.deleteByUser(findOne(id));
        log.info("Deleting user with id '{}'", id);
        userRepository.deleteById(id);
    }
}
