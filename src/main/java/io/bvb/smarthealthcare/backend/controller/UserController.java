package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.exception.PermissionDeniedException;
import io.bvb.smarthealthcare.backend.model.UserResponse;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @GetMapping("/current")
    public UserResponse getUser(HttpSession session) {
        final UserResponse user = CurrentUserData.getUser();
        if (user == null) {
            throw new PermissionDeniedException("User is not logged in!!!!!");
        }
        return user;
    }
}
