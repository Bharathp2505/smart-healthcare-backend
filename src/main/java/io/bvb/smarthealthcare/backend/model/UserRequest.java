package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.Gender;
import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email Id is required")
    @Email
    private String emailId;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "Date of Birth is required")
    @PastOrPresent(message = "Date of Birth must be past")
    private Date dateOfBirth;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Marital Status is required")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private String preConditions;

    private String allergies;

    @NotNull(message = "Blood Group is required")
    private String bloodGroup;

    @NotNull(message = "Emergency Number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid emergency phone number")
    private String emergencyNumber;

    @NotNull(message = "Emergency Name is required ")
    private String emergencyName;
    private boolean isPatient;
}
