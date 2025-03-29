package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientResponse extends UserResponse {
    private String address;
    private MaritalStatus maritalStatus;
    private String preConditions;
    private String allergies;
    private String bloodGroup;
    private String emergencyNumber;
    private String emergencyName;
}
