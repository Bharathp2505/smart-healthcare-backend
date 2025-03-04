package io.bvb.smarthealthcare.backend.entity;


import io.bvb.smarthealthcare.backend.constant.Gender;
import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emailId", unique = true, nullable = false)
    private String emailId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "dateOfBirth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maritalStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String preConditions;

    @Column
    private String allergies;

    @Column(name = "bloodGroup", nullable = false)
    private String bloodGroup;

    @Column(name = "emergencyContactNumber", nullable = false)
    private String emergencyNumber;

    @Column(name = "emergencyContactName", nullable = false)
    private String emergencyName;

    @Column(name = "isPatient")
    private boolean isPatient;

    @Column(name = "isAdmin")
    private boolean isAdmin;
}
