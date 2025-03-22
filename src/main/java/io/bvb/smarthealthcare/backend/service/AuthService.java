package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.*;
import io.bvb.smarthealthcare.backend.exception.AlreadyRegisteredException;
import io.bvb.smarthealthcare.backend.exception.ApplicationException;
import io.bvb.smarthealthcare.backend.exception.InvalidCredentialsException;
import io.bvb.smarthealthcare.backend.model.DoctorRequest;
import io.bvb.smarthealthcare.backend.model.LoginRequest;
import io.bvb.smarthealthcare.backend.model.PatientRequest;
import io.bvb.smarthealthcare.backend.model.ResetPassword;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.PasswordResetTokenRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository contextRepository;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthService(UserRepository userRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder, SecurityContextRepository contextRepository, EmailService emailService, ResetPasswordService resetPasswordService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.contextRepository = contextRepository;
        this.emailService = emailService;
        this.resetPasswordService = resetPasswordService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public void registerPatient(PatientRequest request) {
        verifyEmailExists(request.getEmail());
        verifyPhoneNumberExists(request.getPhoneNumber());

        Patient patient = new Patient();
        patient.setEmail(request.getEmail());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setRole(Role.PATIENT);
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setAllergies(request.getAllergies());
        patient.setMaritalStatus(request.getMaritalStatus());
        patient.setEmergencyName(request.getEmergencyName());
        patient.setGender(request.getGender());
        patient.setPreConditions(request.getPreConditions());
        patient.setEmergencyNumber(request.getEmergencyNumber());
        patient = patientRepository.save(patient);
        try {
            emailService.sendWelcomeEMail(request.getEmail(), request.getFirstName());
        } catch (MessagingException e) {
            LOGGER.error("Failed to send welcome mail", e);
        }
        LOGGER.info("Patient registered successfully" + patient.getId());
    }

    public void registerDoctor(DoctorRequest request) {
        verifyEmailExists(request.getEmail());
        verifyPhoneNumberExists(request.getPhoneNumber());
        verifyLicenseNumberExists(request.getLicenseNumber());

        Doctor doctor = new Doctor();
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setRole(Role.DOCTOR);
        doctor.setGender(request.getGender());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setClinicAddress(request.getClinicAddress());
        doctor.setExperience(request.getExperience());
        doctor.setClinicName(request.getClinicName());
        doctor.setQualification(request.getQualification());
        doctor = doctorRepository.save(doctor);
        try {
            emailService.sendWelcomeEMail(request.getEmail(), request.getFirstName());
        } catch (MessagingException e) {
            LOGGER.error("Failed to send welcome mail", e);
        }
        LOGGER.info("Doctor registered successfully" + doctor.getId());
    }

    private void verifyEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyRegisteredException("Email", email);
        }
    }

    private void verifyPhoneNumberExists(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AlreadyRegisteredException("Phone Number", phoneNumber);
        }
    }

    private void verifyLicenseNumberExists(String licenseNumber) {
        if (doctorRepository.existsByLicenseNumber(licenseNumber)) {
            throw new AlreadyRegisteredException("License Number", licenseNumber);
        }
    }

    public void login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Optional<User> userOptional = userRepository.findByEmailOrPhoneNumber(request.getEmail(), request.getEmail());
        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            LOGGER.error("Invalid Credentials : {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        User user = userOptional.get();

        // Create Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));

        // Populate SecurityContextHolder
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        // Store the SecurityContext in session for subsequent requests
        SecurityContextHolder.setContext(securityContext);
        contextRepository.saveContext(securityContext, httpRequest, httpResponse);


        // Set session attribute
        httpRequest.getSession().setAttribute("user", user);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
    }

    public void forgotPassword(final ResetPassword resetPassword) {
        final User user = userRepository.findByEmail(resetPassword.getEmail()).orElseThrow(() -> new ApplicationException(String.format("%s is not found.", resetPassword.getEmail())));
        if (Role.ADMIN.equals(user.getRole())) {
            throw new ApplicationException("Admin cannot change the password.");
        }
        resetPasswordService.generateResetToken(resetPassword.getEmail(), user.getFirstName());
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException("Invalid or expired token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ApplicationException("Token expired");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new ApplicationException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.deleteByEmail(resetToken.getEmail());
    }
}
