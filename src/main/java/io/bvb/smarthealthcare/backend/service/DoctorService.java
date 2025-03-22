package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.DoctorNotFoundException;
import io.bvb.smarthealthcare.backend.exception.UserNotFoundException;
import io.bvb.smarthealthcare.backend.model.DoctorResponse;
import io.bvb.smarthealthcare.backend.model.TimeSlotRequest;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<DoctorResponse> listDoctors() {
        List<Doctor> doctors = doctorRepository.findAllByDeleted(Boolean.FALSE);
        return convertDoctorsToResponse(doctors);
    }

    private List<DoctorResponse> convertDoctorsToResponse(List<Doctor> doctors) {
        return doctors.stream().map(this::convertDoctorToResponse).collect(Collectors.toCollection(ArrayList::new));
    }

    private DoctorResponse convertDoctorToResponse(Doctor doctor) {
        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setId(doctor.getId());
        doctorResponse.setEmail(doctor.getEmail());
        doctorResponse.setPhoneNumber(doctor.getPhoneNumber());
        doctorResponse.setFirstName(doctor.getFirstName());
        doctorResponse.setLastName(doctor.getLastName());
        doctorResponse.setGender(doctor.getGender());
        doctorResponse.setDateOfBirth(doctor.getDateOfBirth());
        doctorResponse.setClinicName(doctor.getClinicName());
        doctorResponse.setStatus(doctor.getStatus());
        doctorResponse.setExperience(doctor.getExperience());
        doctorResponse.setSpecialization(doctor.getSpecialization());
        doctorResponse.setClinicAddress(doctor.getClinicAddress());
        doctorResponse.setQualification(doctor.getQualification());
        doctorResponse.setLicenseNumber(doctor.getLicenseNumber());
        return doctorResponse;
    }

    @Transactional
    public void deleteDoctor(Long id) {
        final Doctor doctor = getDoctor(id);
        doctor.setDeleted(Boolean.TRUE);
        doctorRepository.save(doctor);
    }

    public Doctor getDoctor(Long id) {
       return doctorRepository.findByIdAndDeleted(id, Boolean.FALSE)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    public List<Appointment> getTodaysAppointments(Long doctorId) {
        List<Appointment> appointments = null;//appointmentRepository.findByDoctorIdAndDate(doctorId, LocalDate.now());
        return appointments;
    }

    @Transactional
    public List<TimeSlot> allocateTimeSlot(TimeSlotRequest request) {
        if (request.getDuration() <= 0 || request.getDuration() > 60) {
            throw new IllegalArgumentException("Invalid slot duration. Must be between 1 and 60 minutes.");
        }

        Doctor doctor = getDoctor(request.getDoctorId());

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

    public List<DoctorResponse> listDoctors(DoctorStatus doctorStatus) {
        if (doctorStatus != null) {
            return convertDoctorsToResponse(doctorRepository.findDoctorsByStatusAndDeleted(doctorStatus, Boolean.FALSE));
        }
        return listDoctors();
    }
}
