package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.exception.AlreadyRegisteredException;
import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.model.PutDoctorRequest;
import io.bvb.smarthealthcare.backend.model.PutPatientRequest;
import io.bvb.smarthealthcare.backend.model.UserResponse;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PatientRepository patientRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void updatePatient(final PutPatientRequest putPatientRequest) {
        final UserResponse userResponse = CurrentUserData.getUser();
        final User user = getUser(userResponse.getEmail());
        validateSelfEdit(userResponse, user);
        validatePatientEmail(user, putPatientRequest);
        validatePatientPhoneNumber(user, putPatientRequest);
        final Patient patient = patientRepository.findById(user.getId()).get();
        patient.setEmail(putPatientRequest.getEmail());
        patient.setPassword(passwordEncoder.encode(putPatientRequest.getPassword()));
        patient.setPhoneNumber(putPatientRequest.getPhoneNumber());
        patient.setAddress(putPatientRequest.getAddress());
        patient.setMaritalStatus(putPatientRequest.getMaritalStatus());
        patient.setPreConditions(putPatientRequest.getPreConditions());
        patient.setAllergies(putPatientRequest.getAllergies());
        patient.setBloodGroup(putPatientRequest.getBloodGroup());
        patient.setEmergencyNumber(putPatientRequest.getEmergencyNumber());
        patient.setEmergencyName(putPatientRequest.getEmergencyName());

        final Patient updatedPatient = patientRepository.saveAndFlush(patient);
        LOGGER.info("Patient is updated successfully : Patient Id : {}, Email : {}", updatedPatient.getId(), updatedPatient.getEmail());
        CurrentUserData.setUser(UserResponse.mapUserToUserResponse(updatedPatient));
    }

    public void updateDoctor(final PutDoctorRequest putDoctorRequest) {
        final UserResponse userResponse = CurrentUserData.getUser();
        final User user = getUser(userResponse.getEmail());
        final Doctor doctor = doctorRepository.findById(user.getId()).get();
        validateSelfEdit(userResponse, user);
        validateDoctorEmail(user, putDoctorRequest);
        validateDoctorPhoneNumber(user, putDoctorRequest);
        doctor.setEmail(putDoctorRequest.getEmail());
        doctor.setPassword(passwordEncoder.encode(putDoctorRequest.getPassword()));
        doctor.setPhoneNumber(putDoctorRequest.getPhoneNumber());
        doctor.setClinicName(putDoctorRequest.getClinicName());
        doctor.setClinicAddress(putDoctorRequest.getClinicAddress());
        doctor.setSpecialization(putDoctorRequest.getSpecialization());
        doctor.setExperience(putDoctorRequest.getExperience());
        doctor.setQualification(putDoctorRequest.getQualification());
        final Doctor updatedDoctor = doctorRepository.saveAndFlush(doctor);
        LOGGER.info("Doctor is updated successfully : Doctor Id : {}, Email : {}", updatedDoctor.getId(), updatedDoctor.getEmail());
        CurrentUserData.setUser(UserResponse.mapUserToUserResponse(updatedDoctor));
    }

    private User getUser(final String username) {
        return userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found with email/phone: " + username));
    }

    private void validateSelfEdit(final UserResponse userResponse, final User user){
        if (userResponse.getId() != user.getId()) {
            LOGGER.error("Your trying to update other user data!!!!");
            throw new InvalidDataException("Your trying to update other user data!!!!");
        }
    }

    private void validatePatientEmail(final User user, final PutPatientRequest putPatientRequest) {
        if (!user.getEmail().equals(putPatientRequest.getEmail())) {
            final Optional<User> patientUser = userRepository.findByEmail(putPatientRequest.getEmail());
            if (patientUser.isPresent() && !Objects.equals(patientUser.get().getId(), user.getId())) {
                LOGGER.error("Email is already registered :: Email : {}", putPatientRequest.getEmail());
                throw new AlreadyRegisteredException("Email", putPatientRequest.getEmail());
            }
        }
    }

    private void validateDoctorEmail(final User user, final PutDoctorRequest putDoctorRequest) {
        if (!user.getEmail().equals(putDoctorRequest.getEmail())) {
            final Optional<User> patientUser = userRepository.findByEmail(putDoctorRequest.getEmail());
            if (patientUser.isPresent() && !Objects.equals(patientUser.get().getId(), user.getId())) {
                LOGGER.error("Email is already registered :: Email : {}", putDoctorRequest.getEmail());
                throw new AlreadyRegisteredException("Email", putDoctorRequest.getEmail());
            }
        }
    }

    private void validatePatientPhoneNumber(final User user, final PutPatientRequest putPatientRequest) {
        if (!user.getPhoneNumber().equals(putPatientRequest.getPhoneNumber())) {
            final Optional<User> patientUser = userRepository.findByPhoneNumber(putPatientRequest.getPhoneNumber());
            if (patientUser.isPresent() && !Objects.equals(patientUser.get().getId(), user.getId())) {
                LOGGER.error("Phone Number is already registered :: Phone Number : {}", putPatientRequest.getPhoneNumber());
                throw new AlreadyRegisteredException("PhoneNumber", putPatientRequest.getPhoneNumber());
            }
        }
    }

    private void validateDoctorPhoneNumber(final User user, final PutDoctorRequest putDoctorRequest) {
        if (!user.getPhoneNumber().equals(putDoctorRequest.getPhoneNumber())) {
            final Optional<User> patientUser = userRepository.findByPhoneNumber(putDoctorRequest.getPhoneNumber());
            if (patientUser.isPresent() && !Objects.equals(patientUser.get().getId(), user.getId())) {
                LOGGER.error("Phone Number is already registered :: Phone Number : {}", putDoctorRequest.getPhoneNumber());
                throw new AlreadyRegisteredException("PhoneNumber", putDoctorRequest.getPhoneNumber());
            }
        }
    }
}
