package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.MedicationTime;
import io.bvb.smarthealthcare.backend.constant.TimeToTake;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PrescriptionRequest {
    @NotNull
    private String medicationName;
    @NotNull
    private String dosage;
    @NotNull
    private Set<MedicationTime> medicationTime = new HashSet<>();
    private TimeToTake timeToTake = TimeToTake.AFTER_FOOD;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;
}

