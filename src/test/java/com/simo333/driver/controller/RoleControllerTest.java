package com.simo333.driver.controller;

import com.simo333.driver.model.Role;
import com.simo333.driver.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoleControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnAllRoles() throws Exception {
        Role role = new Role();
        role.setName(Role.Type.ROLE_ADMIN);
        role.setId(1L);

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        given(roleService.findAll()).willReturn(roles);

        mvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(roles.size())));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnOneRole() throws Exception {
        Role role = new Role();
        role.setName(Role.Type.ROLE_ADMIN);
        Long roleId = 1L;
        role.setId(roleId);

        given(roleService.findOne(roleId)).willReturn(role);

        mvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(role.getName().toString())));
    }

    @Test
    @WithMockUser(username = "username", roles = {"ADMIN"})
    void shouldReturnNotFound() throws Exception {
        Long roleId = 1L;

        given(roleService.findOne(roleId)).willThrow(ResourceNotFoundException.class);

        mvc.perform(get("/api/roles/{id}", roleId))
                .andExpect(status().isNotFound());
    }



}