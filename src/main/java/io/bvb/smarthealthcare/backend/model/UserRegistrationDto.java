package io.bvb.smarthealthcare.backend.model;
import io.bvb.smarthealthcare.backend.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserRegistrationDto {
    private String email;
    private String password;
    private User.Role role; // PATIENT or DOCTOR
}

