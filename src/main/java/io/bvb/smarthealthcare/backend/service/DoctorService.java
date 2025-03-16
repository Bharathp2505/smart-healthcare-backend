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

import java.time.LocalDate;
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

    public void allocateTimeSlot(TimeSlotRequest request) {
        if (timeSlotRepository.findByDoctorIdAndDateAndStartTime(request.getDoctorId(), request.getDate(), request.getStartTime()).isPresent()) {
            throw new RuntimeException("TimeSlotOccupiedException: This time slot is already booked.");
        }

        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDoctor(doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found")));
        timeSlot.setDate(request.getDate());
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setClinicName(request.getClinicName());
        timeSlotRepository.save(timeSlot);
        LOGGER.info("Time slot allocated successfully");
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
