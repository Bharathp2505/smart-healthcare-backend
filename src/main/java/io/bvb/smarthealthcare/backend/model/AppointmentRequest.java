package io.bvb.smarthealthcare.backend.model;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentRequest {
    @NotNull
    private Long patientId;
    @NotNull
    private String timeSlotId;
}