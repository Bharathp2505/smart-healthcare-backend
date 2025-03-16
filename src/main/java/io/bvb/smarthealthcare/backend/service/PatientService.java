package io.bvb.smarthealthcare.backend.service;
import io.bvb.smarthealthcare.backend.entity.Appointment;
import io.bvb.smarthealthcare.backend.model.AppointmentRequest;
import io.bvb.smarthealthcare.backend.repository.AppointmentRepository;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.TimeSlotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

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

    public ResponseEntity<String> bookAppointment(AppointmentRequest request) {
        boolean isTimeSlotBooked = appointmentRepository.findByDoctorIdAndDate(request.getDoctorId(), request.getDate())
                .stream()
                .anyMatch(appointment -> appointment.getTime().equals(request.getTime()));

        if (isTimeSlotBooked) {
            throw new RuntimeException("TimeSlotOccupiedException: This time slot is already booked.");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        appointment.setPatient(patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        appointment.setDate(request.getDate());
        appointment.setTime(request.getTime());
        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment booked successfully");
    }

    public ResponseEntity<List<Appointment>> getUpcomingAppointments(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(patientId, LocalDate.now());
        return ResponseEntity.ok(appointments);
    }
}
