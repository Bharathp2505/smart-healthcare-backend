package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.service.DoctorService;
import io.bvb.smarthealthcare.backend.service.TimeSlotService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;
    private final TimeSlotService timeSlotService;

    public DoctorController(DoctorService doctorService, TimeSlotService timeSlotService) {
        this.doctorService = doctorService;
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public List<DoctorResponse> listDoctors(@RequestParam(name = "status", required = false) DoctorStatus doctorStatus) {
        return doctorService.listDoctors(doctorStatus);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getDoctor(doctorId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponse>> searchDoctors(@RequestParam(name = "search", required = false) String searchString) {
        return ResponseEntity.ok(doctorService.searchDoctorsByClinicNameOrSpecialization(searchString));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<StringResponse> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok().body(new StringResponse("Doctor deleted successfully"));
    }

    @GetMapping("/todays-appointments")
    public List<Appointment> getTodaysAppointments(final HttpSession httpSession) {
        return doctorService.getTodaysAppointments(((UserResponse) httpSession.getAttribute("user")).getId());
    }

    @PostMapping("/allocate-timeslots")
    public TimeSlotResponse allocateTimeSlot(@Valid @RequestBody TimeSlotRequest request) {
        return doctorService.allocateTimeSlot(request);
    }

    @GetMapping("/timeslots/{doctorId}")
    public TimeSlotResponse getTimeslotsByDoctor(@PathVariable Long doctorId) {
        return doctorService.getTimeSlotsByDoctorId(doctorId);
    }
}
