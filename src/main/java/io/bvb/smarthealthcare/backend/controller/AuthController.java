package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.DoctorRequest;
import io.bvb.smarthealthcare.backend.model.LoginRequest;
import io.bvb.smarthealthcare.backend.model.PatientRequest;
import io.bvb.smarthealthcare.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/patient")
    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientRequest request) {
        authService.registerPatient(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<String> registerDoctor(@Valid @RequestBody DoctorRequest request) {
        authService.registerDoctor(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        authService.login(request, httpRequest);
        return ResponseEntity.ok("Login successful!!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }
}
