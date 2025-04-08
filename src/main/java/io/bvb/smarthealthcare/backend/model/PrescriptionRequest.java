package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.TimeOfDay;
import jakarta.validation.constraints.FutureOrPresent;
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
    private String medicationName;
    @NotNull
    private String dosage;
    @NotNull
    private TimeOfDay timeToTake;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;
}

