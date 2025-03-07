package io.bvb.smarthealthcare.backend.entity;
import jakarta.persistence.*;
import lombok.*;

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

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role;

        @Column(nullable = false, updatable = false)
        private String createdAt = java.time.LocalDateTime.now().toString();

        public enum Role {
            PATIENT, DOCTOR, ADMIN
        }
}
