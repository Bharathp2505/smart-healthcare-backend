package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<String> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        return patientService.bookAppointment(request);
    }

    @GetMapping("/upcoming-appointments")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@RequestParam Long patientId) {
        return patientService.getUpcomingAppointments(patientId);
    }
}