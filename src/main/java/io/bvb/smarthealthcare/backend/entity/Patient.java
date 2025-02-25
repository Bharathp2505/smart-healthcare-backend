package io.bvb.smarthealthcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Min(value = 0, message = "Age must be a positive number")
    @Column(name = "age")
    private int Age;

    @NotBlank(message = "Phone number is required")
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @NotNull(message = "Date of Birth is required")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "gender")
    private String gender;

    @Column(name = "preconditions")
    private String preconditions;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "blood_group")
    private String BloodGroup;

    @NotBlank(message = "Emergency number is required")
    @Column(name = "emergency_number", nullable = false)
    private String emergencyNumber;

    @Column(name = "emergency_name")
    private String emergencyName;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "policy_number")
    private String policyNumber;

    @NotNull(message = "Validity date is required")
    @Column(name = "validity_date")
    private LocalDate validityDate;
}

