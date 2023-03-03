package com.cymark.cymarkdelivery.controller;

import com.cymark.cymarkdelivery.dtos.requests.AuthenticationRequest;
import com.cymark.cymarkdelivery.dtos.response.AuthenticationResponse;
import com.cymark.cymarkdelivery.dtos.response.RegistrationResponse;
import com.cymark.cymarkdelivery.service.AuthenticationService;
import com.cymark.cymarkdelivery.dtos.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/addSuperAdmin")
    public ResponseEntity<RegistrationResponse> createSuperAdmin(@RequestBody RegisterRequest request) throws MessagingException {
        return ResponseEntity.ok(service.createSuperAdmin(request));
    }
}