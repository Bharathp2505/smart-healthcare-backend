package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.model.UserLoginDto;
import io.bvb.smarthealthcare.backend.model.UserRegistrationDto;
import io.bvb.smarthealthcare.backend.model.UserResponseDto;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto registerUser(UserRegistrationDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().name());

        return response;
    }

    public UserResponseDto loginUser(UserLoginDto request, HttpSession session) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }

        User user = userOpt.get();
        session.setAttribute("user", user); // Store user session

        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }

    public void logoutUser(HttpSession session) {
        session.invalidate();
    }
}