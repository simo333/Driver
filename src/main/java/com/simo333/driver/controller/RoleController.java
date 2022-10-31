package com.simo333.driver.controller;

import com.simo333.driver.model.Role;
import com.simo333.driver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @GetMapping
    @ResponseStatus(OK)
    public List<Role> getAllRoles() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Role getById(@PathVariable Long id) {
        return service.findOne(id);
    }

}
