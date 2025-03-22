package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPassword implements Serializable {
    @Email
    private String email;
}
