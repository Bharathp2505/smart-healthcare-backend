package io.bvb.smarthealthcare.backend.controller;


import io.bvb.smarthealthcare.backend.constant.LoginUserType;
import io.bvb.smarthealthcare.backend.model.UserRequest;
import io.bvb.smarthealthcare.backend.service.UserService;
import io.bvb.smarthealthcare.backend.service.UseroldService;
import io.bvb.smarthealthcare.backend.util.LoginUserTypeUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutholdController {
    @Autowired
    private UseroldService useroldService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid @RequestBody UserRequest userRequest) {
        useroldService.registerUser(userRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "ADMIN") LoginUserType loginUserType, @RequestParam String emailId, @RequestParam String password) {
        try {
            LoginUserTypeUtil.setLoginUserType(loginUserType);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailId, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } finally {
            LoginUserTypeUtil.unsetLoginUserType();
        }
        return "Login successful";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "Logged out successfully";
    }
}

