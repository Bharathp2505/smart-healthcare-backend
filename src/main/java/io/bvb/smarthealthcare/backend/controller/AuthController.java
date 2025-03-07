package io.bvb.smarthealthcare.backend.controller;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @GetMapping("/success")
    public ResponseEntity<String> loginSuccess(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("Login successful! Welcome, " + userDetails.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
}
