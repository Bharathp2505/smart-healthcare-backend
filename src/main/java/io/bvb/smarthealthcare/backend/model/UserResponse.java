package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.Gender;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
}
