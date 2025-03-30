package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.TimeOfDay;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescriptionRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private String medicationName;
    @NotNull
    private String dosage;
    @NotNull
    private TimeOfDay timeToTake;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}

