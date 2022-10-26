package com.simo333.driver.controller;

import com.simo333.driver.model.Role;
import com.simo333.driver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getAllRoles() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Role getById(@PathVariable Long id) {
        return service.findOne(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Role getByName(@PathVariable String name) {
        return service.findOne(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role save(@RequestBody @Valid Role role) {
        return service.save(role);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Role update(@RequestBody @Valid Role role, @PathVariable Long id) {
        role.setId(id);
        return service.update(role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
