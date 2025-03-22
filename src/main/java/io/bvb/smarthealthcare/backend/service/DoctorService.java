package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.model.TimeSlotRequest;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorService.class);
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;

    public DoctorService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<Appointment> getTodaysAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, LocalDate.now());
        return appointments;
    }

    @Transactional
    public List<TimeSlot> allocateTimeSlot(TimeSlotRequest request) {
        if (request.getDuration() <= 0 || request.getDuration() > 60) {
            throw new IllegalArgumentException("Invalid slot duration. Must be between 1 and 60 minutes.");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime current = request.getStartTime();

        while (current.plusMinutes(request.getDuration()).isBefore(request.getEndTime()) || current.plusMinutes(request.getDuration()).equals(request.getEndTime())) {
            if (timeSlotRepository.existsByDoctorIdAndDateAndStartTime(request.getDoctorId(), request.getDate(), current)) {
                throw new RuntimeException("Time slot already allocated at " + current);
            }

            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDoctor(doctor);
            timeSlot.setDate(request.getDate());
            timeSlot.setStartTime(current);
            timeSlot.setEndTime(current.plusMinutes(request.getDuration()));
            timeSlot.setDuration(request.getDuration());
            timeSlot.setClinicName(request.getClinicName());
            timeSlots.add(timeSlot);

            current = current.plusMinutes(request.getDuration());
        }

        return timeSlotRepository.saveAll(timeSlots);
    }

    public List<Doctor> searchDoctors(String searchString) {
        return doctorRepository.findByClinicNameOrSpecializationContainingIgnoreCase(searchString, searchString);
    }

    public List<Doctor> listDoctors(DoctorStatus doctorStatus) {
        if (doctorStatus != null) {
            doctorRepository.findDoctorsByStatus(doctorStatus);
        }
        return doctorRepository.findAll();
    }
}
