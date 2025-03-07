package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getUpcomingAppointments(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getDailyAppointments(Doctor doctor, LocalDate date) {
        return appointmentRepository.findByDoctorAndAppointmentDate(doctor, date);
    }
}

