package io.bvb.smarthealthcare.backend.repository;


import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.model.PatientResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByDeleted(boolean isDeleted);
    Optional<Patient> findByIdAndDeleted(Long patientId, Boolean isDeleted);
}