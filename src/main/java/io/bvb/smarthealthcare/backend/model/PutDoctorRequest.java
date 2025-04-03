package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PutDoctorRequest extends LoginRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;
    @NotNull(message = "Clinic name is required")
    private String clinicName;
    @NotNull(message = "Clinic address is required")
    private String clinicAddress;
    @NotNull(message = "Specialization is required")
    @Size(message = "Specialization must be at least 2 characters", min = 2)
    private String specialization;
    @NotNull(message = "Experience is required")
    private Long experience;
    @NotNull(message = "Qualification is required")
    private String qualification;
}
