package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.MedicationTime;
import io.bvb.smarthealthcare.backend.constant.TimeToTake;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @Size(min = 1, message = "At least one time of day must be provided")
    private Set<@NotBlank MedicationTime> medicationTime = new HashSet<>();
    private TimeToTake timeToTake = TimeToTake.AFTER_FOOD;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;
}

