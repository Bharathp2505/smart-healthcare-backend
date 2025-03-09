package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
}
