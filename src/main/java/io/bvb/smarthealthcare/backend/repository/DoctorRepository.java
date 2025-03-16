package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseNumber(String licenseNumber);

    List<Doctor> findByClinicNameOrSpecializationContainingIgnoreCase(String clinicName, String specialization);

    List<Doctor> findDoctorsByStatus(DoctorStatus status);
}
