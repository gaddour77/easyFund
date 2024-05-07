package tn.esprit.easyfund.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.config.JwtService;
import tn.esprit.easyfund.entities.*;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.repositories.TokenRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final IUserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final IProfileServices profileServices;
  private final IEmailService emailService;
  private final ISmsService smsService;


  public AuthenticationResponse register(RegisterRequest request) {
    // Prepend "+216" to the phone number if it doesn't already start with it
    String phoneNumber = request.getPhoneNumber();
    if (phoneNumber != null && !phoneNumber.startsWith("+216")) {
      phoneNumber = "+216" + phoneNumber;
    }

    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .salary(request.getSalary())
            .cin(request.getCin())
            .dateOfBirth(request.getDateNaissance())
            .phoneNumber(phoneNumber) // Use the modified phone number
            .build();

    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken((UserDetails) user);
    var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
    profileServices.createProfileForUser(savedUser);
    saveUserToken(savedUser, jwtToken);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );

      var user = repository.findByEmail(request.getEmail())
              .orElseThrow();

      // Check if the user is banned
      if (user.isBanned()) {
        return AuthenticationResponse.builder()
                .errorMessage("You are banned. Please contact support for assistance.")
                .build();
      }

      var jwtToken = jwtService.generateToken((UserDetails) user);
      var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
    } catch (DisabledException e) {
      return AuthenticationResponse.builder()
              .errorMessage("User is banned")
              .build();
    } catch (Exception e) {
      e.printStackTrace();
      return AuthenticationResponse.builder()
              .errorMessage("Authentication failed. Please check your credentials.")
              .build();
    }
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
    var validUserTokens = tokenRepository.findAllValidTokenByUser(Math.toIntExact(user.getUserId()));
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, (UserDetails) user)) {
        var accessToken = jwtService.generateToken((UserDetails) user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
  public void sendValidationCode(String email, ValidationMethod validationMethod) {
    User user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    String validationCode = generateValidationCode();
    user.setPhoneNumber("+21650177848");
    user.setValidationCode(validationCode);
    user.setValidationCodeTimestamp(LocalDateTime.now());
    repository.save(user);

    if (validationMethod == ValidationMethod.SMS) {
      sendValidationCodeBySms(user.getPhoneNumber(), validationCode);
    } else {
      // Handle other validation methods as needed
    }
  }

  private String generateValidationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000); // Generate 6-digit code
    return String.valueOf(code);
  }

  private void sendValidationCodeByEmail(String email, String validationCode) {
    // Send an email with the validation code
    String subject = "Forgot Password Validation Code";
    String message = "Your validation code is: " + validationCode;
    emailService.sendEmail(email, subject, message);
  }

  private void sendValidationCodeBySms(String phoneNumber, String validationCode) {
    // Send an SMS with the validation code
    String message = "Your validation code is: " + validationCode;
    smsService.sendSms(phoneNumber, message);
  }

  public boolean resetPassword(String email, String validationCode, String newPassword) {
    User user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    if (isCodeValid(user, validationCode)) {
      user.setPassword(passwordEncoder.encode(newPassword));
      user.setValidationCode(null); // Clear the code after successful password reset
      user.setValidationCodeTimestamp(null); // Clear the timestamp
      repository.save(user);
      return true;
    }
    return false;
  }

  private boolean isCodeValid(User user, String submittedCode) {
    String userCode = user.getValidationCode();
    LocalDateTime codeTimestamp = user.getValidationCodeTimestamp();
    return userCode != null && userCode.equals(submittedCode) &&
            codeTimestamp != null && codeTimestamp.isAfter(LocalDateTime.now().minusMinutes(10)); // 10-minute validity
  }
}
