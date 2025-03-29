package io.bvb.smarthealthcare.backend.controller;

import io.bvb.smarthealthcare.backend.exception.PermissionDeniedException;
import io.bvb.smarthealthcare.backend.model.UserResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/current")
    public UserResponse getUser(HttpSession session) {
        final UserResponse user = (UserResponse) session.getAttribute("user");
        if (user == null) {
            throw new PermissionDeniedException("User is not logged in!!!!!");
        }
        return (UserResponse) session.getAttribute("user");
    }
}
