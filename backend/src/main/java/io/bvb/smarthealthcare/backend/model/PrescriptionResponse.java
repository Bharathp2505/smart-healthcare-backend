package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.PrescriptionStatus;
import io.bvb.smarthealthcare.backend.constant.TimeOfDay;
import io.bvb.smarthealthcare.backend.entity.Prescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private AppointmentResponse appointmentResponse;
    private DoctorResponse doctorResponse;
    private String id;
    private String medicationName;
    private String dosage;
    private TimeOfDay timeToTake;  // Morning, Afternoon, Night
    private LocalDate startDate;
    private LocalDate endDate;
    private PrescriptionStatus status;

    public static PrescriptionResponse convertEntityToResponse(final Prescription prescription) {
        final PrescriptionResponse prescriptionResponse = new PrescriptionResponse();
        prescriptionResponse.setAppointmentResponse(AppointmentResponse.convertEntityToResponse(prescription.getAppointment()));
        prescriptionResponse.setDoctorResponse(DoctorResponse.convertDoctorToResponse(prescription.getDoctor()));
        prescriptionResponse.setId(prescription.getId());
        prescriptionResponse.setDosage(prescription.getDosage());
        prescriptionResponse.setMedicationName(prescription.getMedicationName());
        prescriptionResponse.setTimeToTake(prescription.getTimeToTake());
        prescriptionResponse.setStartDate(prescription.getStartDate());
        prescriptionResponse.setEndDate(prescription.getEndDate());
        prescriptionResponse.setStatus(prescription.getStatus());
        return prescriptionResponse;
    }
}
