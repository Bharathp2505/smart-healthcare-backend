package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    public TimeSlotService(TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public List<TimeSlot> allocateTimeSlots(Long doctorId, LocalDate date, LocalTime startTime, LocalTime endTime, int duration, String clinicName) {
        if (duration <= 0 || duration > 60) {
            throw new IllegalArgumentException("Invalid slot duration. Must be between 1 and 60 minutes.");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime current = startTime;

        while (current.plusMinutes(duration).isBefore(endTime) || current.plusMinutes(duration).equals(endTime)) {
            if (timeSlotRepository.existsByDoctorIdAndDateAndStartTime(doctorId, date, current)) {
                throw new RuntimeException("Time slot already allocated at " + current);
            }

            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDoctor(doctor);
            timeSlot.setDate(date);
            timeSlot.setStartTime(current);
            timeSlot.setEndTime(current.plusMinutes(duration));
            timeSlot.setDuration(duration);
            timeSlot.setClinicName(clinicName);
            timeSlots.add(timeSlot);

            current = current.plusMinutes(duration);
        }

        return timeSlotRepository.saveAll(timeSlots);
    }

    public List<TimeSlot> getTimeSlotsByDoctor(Long doctorId) {
        return timeSlotRepository.findByDoctorId(doctorId);
    }
}
