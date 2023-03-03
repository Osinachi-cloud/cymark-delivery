package com.cymark.cymarkdelivery.service;

import com.cymark.cymarkdelivery.dtos.requests.AuthenticationRequest;
import com.cymark.cymarkdelivery.dtos.requests.RegisterRequest;
import com.cymark.cymarkdelivery.dtos.response.AuthenticationResponse;
import com.cymark.cymarkdelivery.dtos.response.RegistrationResponse;

import javax.mail.MessagingException;

public interface AuthenticationService {

   RegistrationResponse register(RegisterRequest request);
   RegistrationResponse createSuperAdmin(RegisterRequest request) throws MessagingException;
   AuthenticationResponse authenticate(AuthenticationRequest request);
}
