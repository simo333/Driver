package com.simo333.driver.controller;

import com.simo333.driver.model.User;
import com.simo333.driver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    @ResponseStatus(OK)
    public Page<User> getAllUsers(Pageable page) {
        return service.findAll(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public User getById(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(OK)
    public User getByUsername(@PathVariable String name) {
        return service.findOne(name);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User save(@RequestBody @Valid User user) {
        return service.save(user);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public User update(@RequestBody @Valid User user, @PathVariable Long id) {
        user.setId(id);
        return service.update(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
