package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}