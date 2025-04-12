package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.exception.PermissionDeniedException;
import io.bvb.smarthealthcare.backend.model.PutDoctorRequest;
import io.bvb.smarthealthcare.backend.model.PutPatientRequest;
import io.bvb.smarthealthcare.backend.model.UserResponse;
import io.bvb.smarthealthcare.backend.service.UserService;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public UserResponse getUser(HttpSession session) {
        final UserResponse user = CurrentUserData.getUser();
        if (user == null) {
            throw new PermissionDeniedException("User is not logged in!!!!!");
        }
        return user;
    }

    /*@PutMapping("/admin")
    public UserResponse editAdmin(@Valid @RequestBody final PutAdminRequest putAdminRequest, final HttpSession httpSession) {
        userService.updateAdmin(putAdminRequest);
        httpSession.setAttribute("user", CurrentUserData.getUser());
        return CurrentUserData.getUser();
    }*/

    @PutMapping("/patient")
    public UserResponse editPatient(@Valid @RequestPart("data") PutPatientRequest putPatientRequest, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage, final HttpSession httpSession) {
        userService.updatePatient(putPatientRequest, profileImage);
        httpSession.setAttribute("user", CurrentUserData.getUser());
        return CurrentUserData.getUser();
    }

    @PutMapping("/doctor")
    public UserResponse editDoctor(@Valid @RequestPart("data") PutDoctorRequest putDoctorRequest, @RequestPart(value = "profileImage", required = false) MultipartFile profileImage, final HttpSession httpSession) {
        userService.updateDoctor(putDoctorRequest, profileImage);
        httpSession.setAttribute("user", CurrentUserData.getUser());
        return CurrentUserData.getUser();
    }
}
