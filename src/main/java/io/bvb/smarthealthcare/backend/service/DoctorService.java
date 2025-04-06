package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.constant.LoginUserType;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.DoctorNotFoundException;
import io.bvb.smarthealthcare.backend.model.DoctorResponse;
import io.bvb.smarthealthcare.backend.model.TimeSlotRequest;
import io.bvb.smarthealthcare.backend.model.TimeSlotResponse;
import io.bvb.smarthealthcare.backend.model.UserResponse;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        return doctors.stream().map(DoctorResponse::convertDoctorToResponse).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void deleteDoctor(Long id) {
        final Doctor doctor = getDoctorById(id);
        doctor.setDeleted(Boolean.TRUE);
        doctorRepository.save(doctor);
    }

    public DoctorResponse getDoctor(Long id) {
        return DoctorResponse.convertDoctorToResponse(getDoctorById(id));
    }

    public List<Appointment> getTodaysAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctorIdAndDate(doctorId, LocalDate.now());
        return appointments;
    }

    @Transactional
    public TimeSlotResponse allocateTimeSlot(TimeSlotRequest request) {
        final UserResponse userResponse = CurrentUserData.getUser();
        if (LocalDate.now().isAfter(request.getDate())) {
            throw new IllegalArgumentException("Invalid date format");
        }
        if (request.getDuration() <= 0 || request.getDuration() > 60) {
            throw new IllegalArgumentException("Invalid slot duration. Must be between 1 and 60 minutes.");
        }
        Doctor doctor = getDoctorById(userResponse.getId());

        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime current = request.getStartTime();

        while (current.plusMinutes(request.getDuration()).isBefore(request.getEndTime()) || current.plusMinutes(request.getDuration()).equals(request.getEndTime())) {
            if (timeSlotRepository.existsByDoctorIdAndDateAndStartTime(userResponse.getId(), request.getDate(), current)) {
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
        timeSlotRepository.saveAll(timeSlots);
        return getTimeSlotsByDoctorId(doctor.getId());
    }

    public TimeSlotResponse getTimeSlotsByDoctorId(Long doctorId) {
        final Doctor doctor = getDoctorById(doctorId);
        final List<TimeSlot> timeSlots = timeSlotRepository.findByDoctorId(doctorId);
        final TimeSlotResponse timeSlotResponse = new TimeSlotResponse();
        timeSlotResponse.setDoctorId(doctorId);
        timeSlotResponse.setSpecialization(doctor.getSpecialization());
        timeSlotResponse.setFirstName(doctor.getFirstName());
        timeSlotResponse.setLastName(doctor.getLastName());

        timeSlotResponse.setTimeSlots(this.convertTimeSlotsToResponse(timeSlots));
        return timeSlotResponse;
    }

    private List<io.bvb.smarthealthcare.backend.model.TimeSlot> convertTimeSlotsToResponse(List<TimeSlot> timeSlots) {
        return timeSlots.stream().map(timeSlot -> {
            final io.bvb.smarthealthcare.backend.model.TimeSlot timeSlot1 = new io.bvb.smarthealthcare.backend.model.TimeSlot();
            timeSlot1.setId(timeSlot.getId());
            timeSlot1.setStartTime(timeSlot.getStartTime());
            timeSlot1.setEndTime(timeSlot.getEndTime());
            timeSlot1.setBooked(timeSlot.isBooked());
            timeSlot1.setClinicName(timeSlot.getClinicName());
            timeSlot1.setDate(timeSlot.getDate());
            return timeSlot1;
        }).sorted(Comparator.comparing(io.bvb.smarthealthcare.backend.model.TimeSlot::getStartTime)).collect(Collectors.toList());
    }


    public List<DoctorResponse> searchDoctorsByClinicNameOrSpecialization(String searchString) {
        return convertDoctorsToResponse(doctorRepository.findByClinicNameOrSpecializationContainingIgnoreCase(searchString, searchString));
    }

    public List<DoctorResponse> listDoctors(DoctorStatus doctorStatus) {
        if (doctorStatus != null) {
            return convertDoctorsToResponse(doctorRepository.findDoctorsByStatusAndDeleted(doctorStatus, Boolean.FALSE));
        }
        return listDoctors();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findByIdAndDeleted(id, Boolean.FALSE)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }
}
