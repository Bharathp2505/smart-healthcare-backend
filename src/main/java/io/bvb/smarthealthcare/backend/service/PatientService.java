package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.PatientNotFoundException;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.model.PatientResponse;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PatientRepository patientRepository;

    public PatientService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.patientRepository = patientRepository;
    }

    public List<PatientResponse> listPatients() {
        return convertPatientToPatientResponses(patientRepository.findAllByDeleted(Boolean.FALSE));
    }

    public PatientResponse getPatient(Long id) {
        return convertPatientToPatientResponse(getPatientById(id));
    }

    @Transactional
    public void deletePatient(Long id) {
        final Patient patient = getPatientById(id);
        patient.setDeleted(Boolean.TRUE);
        patientRepository.save(patient);
    }

    public ResponseEntity<String> bookAppointment(final AppointmentRequest appointmentRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(appointmentRequest.getTimeSlotId()).orElseThrow(() -> new RuntimeException("Time slot not found"));

        if (timeSlot.isBooked()) {
            throw new RuntimeException("Time slot already booked");
        }

        Patient patient = patientRepository.findById(appointmentRequest.getPatientId()).orElseThrow(() -> new RuntimeException("Patient not found"));
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setTimeSlot(timeSlot);

        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment booked successfully");
    }

    public ResponseEntity<List<Appointment>> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = null;//appointmentRepository.findByDoctorIdAndDate(patientId, LocalDate.now());
        return ResponseEntity.ok(appointments);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findByIdAndDeleted(id, Boolean.FALSE)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    private List<PatientResponse> convertPatientToPatientResponses(List<Patient> patients) {
        return patients.stream().map(this::convertPatientToPatientResponse).collect(Collectors.toList());
    }

    private PatientResponse convertPatientToPatientResponse(Patient patient) {
        final PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patient.getId());
        patientResponse.setEmail(patient.getEmail());
        patientResponse.setPhoneNumber(patient.getPhoneNumber());
        patientResponse.setFirstName(patient.getFirstName());
        patientResponse.setLastName(patient.getLastName());
        patientResponse.setGender(patient.getGender());
        patientResponse.setDateOfBirth(patient.getDateOfBirth());
        patientResponse.setAddress(patient.getAddress());
        patientResponse.setMaritalStatus(patient.getMaritalStatus());
        patientResponse.setEmergencyNumber(patient.getEmergencyNumber());
        patientResponse.setEmergencyName(patient.getEmergencyName());
        patientResponse.setAllergies(patient.getAllergies());
        patientResponse.setBloodGroup(patient.getBloodGroup());
        patientResponse.setMaritalStatus(patient.getMaritalStatus());
        patientResponse.setPreConditions(patient.getPreConditions());
        return patientResponse;
    }
}
