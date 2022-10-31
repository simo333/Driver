package com.simo333.driver.service;


import com.simo333.driver.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findOne(Role.Type type);

    Role findOne(Long userId);

    Role save(Role role);

    Role update(Role role);

    void delete(Long id);
}
