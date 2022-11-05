package com.simo333.driver;

import com.simo333.driver.model.Role;
import com.simo333.driver.model.User;
import com.simo333.driver.service.RoleService;
import com.simo333.driver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class DriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class, args);

    }

    @Bean
    CommandLineRunner run(RoleService roleService, UserService userService) {
        return args -> {
            Role roleUser = new Role();
            roleUser.setName(Role.Type.ROLE_USER);
            Role roleAdmin = new Role();
            roleAdmin.setName(Role.Type.ROLE_ADMIN);

            roleService.save(roleUser);
            roleService.save(roleAdmin);

//            userService.save(User.builder()
//                    .username("szymon")
//                    .email("szymon.333@wp.pl")
//                    .password("12345678")
//                    .enabled(true)
//                    .roles(Set.of(roleService.findOne(Role.Type.ROLE_USER)))
//                    .build());
        };
    }

}
