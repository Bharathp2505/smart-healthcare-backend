package io.bvb.smarthealthcare.backend.service;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }
}

