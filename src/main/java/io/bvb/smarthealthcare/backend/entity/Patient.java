package io.bvb.smarthealthcare.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Email is required")
    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;
}

