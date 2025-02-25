package io.bvb.smarthealthcare.backend.service;


import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.model.PatientRequest;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Save Patient
    public Patient savePatient(PatientRequest patientRequest) {
        final Patient patient = new Patient();
//        patient.setAge(patientRequest.getAge());
//        patient.setEmail(patientRequest.getEmail());
//        patient.setAddress(patientRequest.getAddress());
//        patient.setAllergies(patientRequest.getAllergies());
//        patient.setGender(patientRequest.getGender());
//        patient.setBloodGroup(patientRequest.getBloodGroup());
//        patient.setDateOfBirth(patientRequest.getDateOfBirth());
//        patient.setEmergencyName(patientRequest.getEmergencyName());
//        patient.setEmergencyNumber(patientRequest.getEmergencyNumber());
//        patient.setInsuranceProvider(patientRequest.getInsuranceProvider());
//        patient.setFirstName(patientRequest.getFirstName());
//        patient.setLastName(patientRequest.getLastName());
//        patient.setPassword(patientRequest.getPassword());
//        patient.setPhoneNumber(patientRequest.getPhoneNumber());
//        patient.setPolicyNumber(patientRequest.getPolicyNumber());
//        patient.setPreconditions(patientRequest.getPreconditions());
//        patient.setValidityDate(patientRequest.getValidityDate());
        return patientRepository.save(patient);
    }

    // Get All Patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get Patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Delete Patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}

