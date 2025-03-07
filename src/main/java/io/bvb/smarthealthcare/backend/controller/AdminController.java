package io.bvb.smarthealthcare.backend.controller;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorService doctorService;

    public AdminController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/pending-doctors")
    public ResponseEntity<List<Doctor>> getPendingDoctors() {
        return ResponseEntity.ok(doctorService.getPendingDoctors());
    }

    @PutMapping("/approve-doctor/{doctorId}")
    public ResponseEntity<Doctor> approveDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.approveDoctor(doctorId));
    }

    @PutMapping("/reject-doctor/{doctorId}")
    public ResponseEntity<Doctor> rejectDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.rejectDoctor(doctorId));
    }
}

