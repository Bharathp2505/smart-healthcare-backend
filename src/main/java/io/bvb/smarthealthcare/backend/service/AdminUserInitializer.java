package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.Gender;
import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        if (!userRepository.existsByEmailId(ADMIN_USERNAME)) {
            User admin = new User();
            admin.setEmailId(ADMIN_USERNAME);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setAdmin(true);
            admin.setMaritalStatus(MaritalStatus.SINGLE);
            admin.setEmergencyNumber("111111111");
            admin.setFirstName("Admin");
            admin.setLastName("A");
            admin.setGender(Gender.OTHER);
            admin.setAddress("Admin Street. India");
            admin.setPhoneNumber("5555555555");
            admin.setAllergies("N/A");
            admin.setDateOfBirth(new Date(2000/10/19));
            admin.setBloodGroup("Blood Group");
            admin.setEmergencyName("Emergency Name");
            // Encode password
            userRepository.save(admin);
            System.out.println("Admin user created!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
