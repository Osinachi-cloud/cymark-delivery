package com.cymark.cymarkdelivery.service.serviceImpl;


import com.cymark.cymarkdelivery.config.JwtService;
import com.cymark.cymarkdelivery.dtos.requests.AuthenticationRequest;
import com.cymark.cymarkdelivery.dtos.requests.RegisterRequest;
import com.cymark.cymarkdelivery.dtos.response.AuthenticationResponse;
import com.cymark.cymarkdelivery.dtos.response.RegistrationResponse;
import com.cymark.cymarkdelivery.enums.TokenType;
import com.cymark.cymarkdelivery.model.Role;
import com.cymark.cymarkdelivery.model.Token;
import com.cymark.cymarkdelivery.model.User;
import com.cymark.cymarkdelivery.repository.TokenRepository;
import com.cymark.cymarkdelivery.repository.UserRepository;
import com.cymark.cymarkdelivery.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;
    private final String message = "<!DOCTYPE = html> \n" +
            "    <html> \n" +
            "        <head> </head>\n" +
            "        <body> \n" +
            "                <h4 style = 'color:blue; text-align: center;' >DRP FILE DOWNLOAD PORTAL</h4>\n" +
            "                <p style = 'color: green;' >Please download the requested file by clicking any of the buttons below</p>\n" +
            "                <p style = 'color: red; font-size: 1.3vw;' >For SFTP file download, right-click and select <b>'Open Link in New Tab'</b></p>\n" +
            "                <a href=\"http://localhost:8080/files/download/" +
            "\"><button style = 'width: 50%; height: 50px; cursor: pointer; margin-left: 25%; color: white; background: #e60000; border-radius: 5px; border:none; cursor:pointer;' target='_self' type = 'text/html'>SFTP File Download</button></a> <br><br>\n" +
            "                <a href=\"http://localhost:8080/files/direct-download/" +
            " \"><button style = 'width: 50%; height: 50px; cursor: pointer; margin-left: 25%; color: white; background: #e60000; border-radius: 5px; border:none; cursor:pointer;'>Direct File Download</button></a>\n" +
            "        </body>\n" +                "    </html>";

    @Transactional
    @Override
    public RegistrationResponse register(RegisterRequest request) {
        String email = request.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent()){
            return RegistrationResponse.builder()
                    .message("Email already exists")
                    .statusCode(400)
                    .timeStamp(new Timestamp(System.currentTimeMillis()))
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Role role = Role.builder()
                .createdAt( new Timestamp(System.currentTimeMillis()))
                .presentRole("INITIATOR")
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .build();
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return RegistrationResponse.builder()
                .message("successful")
                .statusCode(HttpStatus.CREATED.value())
                .timeStamp(new Timestamp(System.currentTimeMillis()))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Transactional
    @Override
    public RegistrationResponse createSuperAdmin(RegisterRequest request) throws MessagingException {

        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isPresent()){
            return RegistrationResponse.builder()
                    .timeStamp(new Timestamp(System.currentTimeMillis()))
                    .statusCode(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Email already exists")
                    .build();
        }

        Role role = Role.builder()
                .createdAt( new Timestamp(System.currentTimeMillis()))
                .presentRole("INITIATOR")
                .build();

        User superAdmin = User.builder()
                .email(request.getEmail())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .build();

        userRepository.save(superAdmin);
        emailService.sendEmail(request.getEmail(), "Super Admin Created", "Welcome, you have been made a super Admin");

        return RegistrationResponse.builder()
                .timeStamp(new Timestamp(System.currentTimeMillis()))
                .message("Successful")
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .build();

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}