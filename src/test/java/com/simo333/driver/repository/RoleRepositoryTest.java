package com.simo333.driver.repository;

import com.simo333.driver.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindRoles() {
        // given
        Role expectedRole = new Role();
        expectedRole.setName(Role.Type.ROLE_USER);
        Role notExpectedRole = new Role();
        notExpectedRole.setName(Role.Type.ROLE_ADMIN);

        // when
        roleRepository.save(expectedRole);
        roleRepository.save(notExpectedRole);
        Optional<Role> actualRole = roleRepository.findByName(Role.Type.ROLE_USER);

        // then
        assertThat(actualRole).isEqualTo(Optional.of(expectedRole));
    }
}