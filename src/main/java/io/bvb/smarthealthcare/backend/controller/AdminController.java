package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/approve-doctor/{doctorId}")
    public ResponseEntity<String> approveDoctor(@PathVariable Long doctorId) {
        adminService.approveDoctor(doctorId);
        return ResponseEntity.ok("Doctor approved successfully");
    }

    @PutMapping("/reject-doctor/{doctorId}")
    public ResponseEntity<String> rejectDoctor(@PathVariable Long doctorId) {
        adminService.rejectDoctor(doctorId);
        return ResponseEntity.ok().body("Doctor rejected");
    }
}
