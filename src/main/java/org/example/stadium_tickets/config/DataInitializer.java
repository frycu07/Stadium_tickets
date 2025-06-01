package org.example.stadium_tickets.config;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles and admin user already exist in the database
        // If they do, skip initialization to avoid unnecessary queries
        if (roleRepository.count() > 0 && userService.existsByUsername("admin")) {
            logger.info("Roles and admin user already exist, skipping initialization");
            return;
        }

        initRoles();
        createAdminUser();
    }

    private void initRoles() {
        try {
            if (!roleRepository.existsByName("USER")) {
                Role userRole = new Role("USER");
                roleRepository.save(userRole);
                logger.info("Created USER role");
            }

            if (!roleRepository.existsByName("ADMIN")) {
                Role adminRole = new Role("ADMIN");
                roleRepository.save(adminRole);
                logger.info("Created ADMIN role");
            }
        } catch (Exception e) {
            logger.error("Error initializing roles: {}", e.getMessage());
        }
    }

    private void createAdminUser() {
        try {
            if (!userService.existsByUsername("admin")) {
                User adminUser = new User("admin", "admin", "admin@example.com");
                User createdUser = userService.createUser(adminUser);
                userService.addAdminRole(createdUser.getId());
                logger.info("Created admin user with username: admin and password: admin");
            }
        } catch (Exception e) {
            logger.error("Error creating admin user: {}", e.getMessage());
        }
    }
}
