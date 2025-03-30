package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.constant.TimeOfDay;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.Prescription;
import io.bvb.smarthealthcare.backend.repository.PrescriptionRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final NotificationService notificationService;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, DoctorService doctorService, PatientService patientService, NotificationService notificationService) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Prescription prescribeMedication(Long doctorId, Long patientId, String medicationName, String dosage, TimeOfDay timeToTake, LocalDate startDate, LocalDate endDate) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        Patient patient = patientService.getPatientById(patientId);
        final Prescription prescription = new Prescription();
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setMedicationName(medicationName);
        prescription.setDosage(dosage);
        prescription.setTimeToTake(timeToTake);
        prescription.setStartDate(startDate);
        prescription.setEndDate(endDate);
        notificationService.sendNotification(patientId, "doctor.patient.prescription.added", new Object[]{patient.getFirstName(), doctor.getFirstName()});
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}

