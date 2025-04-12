package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register/patient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StringResponse> registerPatient(@Valid @RequestPart("data") PatientRequest request, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        authService.registerPatient(request, profileImage);
        return ResponseEntity.ok(new StringResponse("Patient Registered Successfully!!"));
    }

    @PostMapping(value = "/register/doctor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StringResponse> registerDoctor(@Valid @RequestPart("data") DoctorRequest request, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        authService.registerDoctor(request, profileImage);
        return ResponseEntity.ok(new StringResponse("Doctor Registered Successfully!!"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.login(request, httpRequest, httpServletResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<StringResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(new StringResponse("Logout successful!!"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<StringResponse> forgotPassword(@Valid @RequestBody ResetPassword request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(new StringResponse("Reset password email sent!"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<StringResponse> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(new StringResponse("Password reset successfully!"));
    }
}
