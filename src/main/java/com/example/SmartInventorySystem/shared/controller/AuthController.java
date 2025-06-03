package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.config.MyUserDetails;
import com.example.SmartInventorySystem.shared.dto.AuthRequestDTO;
import com.example.SmartInventorySystem.user.entity.User;
import com.example.SmartInventorySystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final UserService userService;


    AuthenticationManager authenticationManager;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User has been created");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();


            return ResponseEntity.ok(Map.of(
                    "username", userDetails.getUsername(),
                    "id", userDetails.getUserId(),
                    "role", userDetails.getRole()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }
}
