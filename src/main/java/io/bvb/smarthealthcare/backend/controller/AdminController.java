package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.model.DoctorResponse;
import io.bvb.smarthealthcare.backend.model.PatientResponse;
import io.bvb.smarthealthcare.backend.model.StringResponse;
import io.bvb.smarthealthcare.backend.service.AdminService;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import io.bvb.smarthealthcare.backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AdminController(AdminService adminService, DoctorService doctorService, PatientService patientService) {
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
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

    @GetMapping("/doctors")
    public List<DoctorResponse> listDoctors() {
        return doctorService.listDoctors();
    }

    @GetMapping("/patients")
    public List<PatientResponse> listPatients() {
        return patientService.listPatients();
    }

    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<StringResponse> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok().body(new StringResponse("Doctor deleted successfully"));
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity<StringResponse> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok().body(new StringResponse("Patient deleted successfully"));
    }
}
