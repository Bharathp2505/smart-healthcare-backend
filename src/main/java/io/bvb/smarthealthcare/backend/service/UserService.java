package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.exception.EmailIdNotAvailableException;
import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.exception.InvalidLoginUserTypeException;
import io.bvb.smarthealthcare.backend.model.UserRequest;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import io.bvb.smarthealthcare.backend.util.LoginUserTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String PHONE_NUMBER_ALREADY_REGISTERED = "Phone number already registered : ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(final UserRequest userRequest) {
        if (userRepository.existsByEmailId(userRequest.getEmailId())) {
            LOGGER.error("User with emailId : {} already exists", userRequest.getEmailId());
            throw new EmailIdNotAvailableException(userRequest.getEmailId());
        }
        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            LOGGER.error("User with phone number : {} already exists", userRequest.getPhoneNumber());
            throw new InvalidDataException(PHONE_NUMBER_ALREADY_REGISTERED + userRequest.getPhoneNumber());
        }
        User user = new User();
        user.setEmailId(userRequest.getEmailId());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setAllergies(userRequest.getAllergies());
        user.setAddress(userRequest.getAddress());
        user.setBloodGroup(userRequest.getBloodGroup());
        user.setGender(userRequest.getGender());
        user.setEmergencyName(userRequest.getEmergencyName());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPreConditions(user.getPreConditions());
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmergencyNumber(userRequest.getEmergencyNumber());
        user.setMaritalStatus(userRequest.getMaritalStatus());
        user.setPatient(userRequest.isPatient());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        final User user = Optional.ofNullable(userRepository.findByEmailId(emailId)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.isAdmin() && (LoginUserTypeUtil.getLoginUserType().name().equals("PATIENT") || LoginUserTypeUtil.getLoginUserType().name().equals("DOCTOR"))) {
            throw new InvalidLoginUserTypeException(LoginUserTypeUtil.getLoginUserType().name());
        } else if (user.isPatient() && (LoginUserTypeUtil.getLoginUserType().name().equals("ADMIN") || LoginUserTypeUtil.getLoginUserType().name().equals("DOCTOR"))) {
            throw new InvalidLoginUserTypeException(LoginUserTypeUtil.getLoginUserType().name());
        } else if (LoginUserTypeUtil.getLoginUserType().name().equals("ADMIN") || LoginUserTypeUtil.getLoginUserType().name().equals("PATIENT")) {
            throw new InvalidLoginUserTypeException(LoginUserTypeUtil.getLoginUserType().name());
        }
        return org.springframework.security.core.userdetails.User.builder().username(user.getEmailId()).password(user.getPassword()).roles("USER")  // You can change roles as needed
                .build();
    }
}

