package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.DoctorRequest;
import io.bvb.smarthealthcare.backend.model.LoginRequest;
import io.bvb.smarthealthcare.backend.model.PatientRequest;
import io.bvb.smarthealthcare.backend.model.StringResponse;
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
    public ResponseEntity<StringResponse> registerPatient(@Valid @RequestBody PatientRequest request) {
        authService.registerPatient(request);
        return ResponseEntity.ok(new StringResponse("Patient Registered Successfully!!"));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<StringResponse> registerDoctor(@Valid @RequestBody DoctorRequest request) {
        authService.registerDoctor(request);
        return ResponseEntity.ok(new StringResponse("Doctor Registered Successfully!!"));
    }

    @PostMapping("/login")
    public ResponseEntity<StringResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) {
        authService.login(request, httpRequest, httpServletResponse);
        return ResponseEntity.ok(new StringResponse("Login successful!!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<StringResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(new StringResponse("Logout successful!!"));
    }
}
