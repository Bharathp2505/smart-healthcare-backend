package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.model.TimeSlotRequest;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/todays-appointments")
    public List<Appointment> getTodaysAppointments(@RequestParam Long doctorId) {
        return doctorService.getTodaysAppointments(doctorId);
    }

    @PostMapping("/allocate-timeslot")
    public ResponseEntity allocateTimeSlot(@RequestBody TimeSlotRequest request) {
        doctorService.allocateTimeSlot(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(doctorService.searchDoctors(specialization));
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> listDoctors(@RequestParam(name = "doctorStatus") DoctorStatus doctorStatus) {
        return ResponseEntity.ok(doctorService.listDoctors(doctorStatus));
    }
}
