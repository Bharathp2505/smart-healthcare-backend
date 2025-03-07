package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByStatus(Doctor.Status status);
}