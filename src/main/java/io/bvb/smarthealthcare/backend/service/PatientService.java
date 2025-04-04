package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.PatientNotFoundException;
import io.bvb.smarthealthcare.backend.exception.TimeSlotNotFoundException;
import io.bvb.smarthealthcare.backend.exception.TimeSlotOccupiedException;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.model.PatientResponse;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);
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
        return PatientResponse.convertPatientToPatientResponse(getPatientById(id));
    }

    @Transactional
    public void deletePatient(Long id) {
        final Patient patient = getPatientById(id);
        patient.setDeleted(Boolean.TRUE);
        patientRepository.save(patient);
    }

    public String bookAppointment(final AppointmentRequest appointmentRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(appointmentRequest.getTimeSlotId())
                .orElseThrow(() -> {
                    LOGGER.error("Time slot not found : {}", appointmentRequest.getTimeSlotId() );
                    throw new TimeSlotNotFoundException(appointmentRequest.getTimeSlotId());
                });
        if (timeSlot.isBooked()) {
            LOGGER.error("Timeslot is already booked :: TimeSlot Id : {}", appointmentRequest.getTimeSlotId());
            throw new TimeSlotOccupiedException();
        }
        timeSlot.setBooked(true);
        timeSlotRepository.save(timeSlot);

        Appointment appointment = new Appointment();
        appointment.setPatient(patientRepository.findById(CurrentUserData.getUser().getId())
                .orElseThrow(() -> {
                    LOGGER.error("Patient not found : {}", CurrentUserData.getUser().getId());
                    throw new PatientNotFoundException(CurrentUserData.getUser().getId());}));
        appointment.setTimeSlot(timeSlot);

        appointmentRepository.save(appointment);
        return "Appointment booked successfully.";
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
        return patients.stream().map(PatientResponse::convertPatientToPatientResponse).collect(Collectors.toList());
    }
}