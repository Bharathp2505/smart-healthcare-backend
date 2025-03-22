package io.bvb.smarthealthcare.backend.service;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.TimeSlot;
import io.bvb.smarthealthcare.backend.exception.TimeSlotOccupiedException;
import io.bvb.smarthealthcare.backend.exception.UserNotFoundException;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public PatientService(AppointmentRepository appointmentRepository, TimeSlotRepository timeSlotRepository,
                          DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<String> bookAppointment(final AppointmentRequest appointmentRequest) {
        TimeSlot timeSlot = timeSlotRepository.findById(appointmentRequest.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("Time slot not found"));

        if (appointmentRepository.existsByTimeSlot(timeSlot)) {
            throw new RuntimeException("Time slot already booked");
        }

        Patient patient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setTimeSlot(timeSlot);

        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment booked successfully");
    }

    public ResponseEntity<List<Appointment>> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(patientId, LocalDate.now());
        return ResponseEntity.ok(appointments);
    }
}
