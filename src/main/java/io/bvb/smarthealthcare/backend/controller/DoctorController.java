package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.model.DoctorResponse;
import io.bvb.smarthealthcare.backend.model.TimeSlotRequest;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import io.bvb.smarthealthcare.backend.service.TimeSlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final TimeSlotService timeSlotService;

    public DoctorController(DoctorService doctorService, TimeSlotService timeSlotService) {
        this.doctorService = doctorService;
        this.timeSlotService = timeSlotService;
    }
    @GetMapping
    public List<DoctorResponse> listDoctors(@RequestParam(name = "doctorStatus", required = false) DoctorStatus doctorStatus) {
        return doctorService.listDoctors(doctorStatus);
    }

    @GetMapping("/todays-appointments")
    public List<Appointment> getTodaysAppointments(@RequestParam Long doctorId) {
        return doctorService.getTodaysAppointments(doctorId);
    }

    @PostMapping("/allocate-timeslot")
    public ResponseEntity<List<TimeSlot>> allocateTimeSlot(@RequestBody TimeSlotRequest request) {
        return ResponseEntity.ok(doctorService.allocateTimeSlot(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> searchDoctors(@RequestParam(required = false) String specialization) {
        return ResponseEntity.ok(doctorService.searchDoctors(specialization));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByDoctor(@PathVariable Long doctorId) {
        List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsByDoctor(doctorId);
        return ResponseEntity.ok(timeSlots);
    }
}
