package io.bvb.smarthealthcare.backend.controller;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final AppointmentService appointmentService;

    public PatientController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.bookAppointment(appointment));
    }

    @GetMapping("/upcoming-appointments")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@RequestBody Patient patient) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(patient));
    }
}
