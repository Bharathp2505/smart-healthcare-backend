package io.bvb.smarthealthcare.backend.service;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findByStatus(Doctor.Status.PENDING);
    }

    public Doctor approveDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(Doctor.Status.APPROVED);
        return doctorRepository.save(doctor);
    }

    public Doctor rejectDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setStatus(Doctor.Status.REJECTED);
        return doctorRepository.save(doctor);
    }
}

