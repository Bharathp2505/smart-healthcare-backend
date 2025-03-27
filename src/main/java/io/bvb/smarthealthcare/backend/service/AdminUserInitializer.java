package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.Gender;
import io.bvb.smarthealthcare.backend.entity.Role;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    private static final String ADMIN_USERNAME = "admin@example.com";
    private static final String ADMIN_PASSWORD = "admin@123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (!userRepository.existsByEmail(ADMIN_USERNAME)) {
            User admin = new User();
            admin.setEmail(ADMIN_USERNAME);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setFirstName("Administrator");
            admin.setLastName("");
            admin.setGender(Gender.OTHER);
            admin.setPhoneNumber("5555555555");
            admin.setDateOfBirth(new Date(2000 / 10 / 19));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
