package com.simo333.driver.controller;

import com.simo333.driver.model.Role;
import com.simo333.driver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/name/{name}")
    @ResponseStatus(OK)
    public Role getByName(@PathVariable String name) {
        return service.findOne(name);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Role save(@RequestBody @Valid Role role) {
        return service.save(role);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public Role update(@RequestBody @Valid Role role, @PathVariable Long id) {
        role.setId(id);
        return service.update(role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
