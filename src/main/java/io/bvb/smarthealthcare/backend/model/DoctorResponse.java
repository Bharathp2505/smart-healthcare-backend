package io.bvb.smarthealthcare.backend.model;


import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorResponse extends UserResponse {
    private String clinicAddress;
    private String specialization;
    private String licenseNumber;
    private String clinicName;
    private Long experience;
    private String qualification;
    private DoctorStatus status;
}
