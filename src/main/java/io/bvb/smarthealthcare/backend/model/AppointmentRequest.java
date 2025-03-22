package io.bvb.smarthealthcare.backend.model;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private Long timeSlotId;
}