package tn.esprit.easyfund.services;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.easyfund.entities.*;
import tn.esprit.easyfund.config.JwtService;
import tn.esprit.easyfund.repositories.TokenRepository;
import tn.esprit.easyfund.repositories.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
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
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
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
    // Generate a validation code
    String validationCode = generateValidationCode();

    // Store validation code and timestamp in user entity
    Optional<User> optionalUser = repository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setValidationCode(validationCode);
      user.setValidationCodeTimestamp(LocalDateTime.now()); // Store current timestamp
      repository.save(user);

      // Send the validation code based on the chosen method
      if (validationMethod == ValidationMethod.EMAIL) {
        sendValidationCodeByEmail(email, validationCode);
      } else if (validationMethod == ValidationMethod.SMS) {
        sendValidationCodeBySms(user.getPhoneNumber(), validationCode);
      }
    } else {
      throw new RuntimeException("User not found");
    }
  }

  private String generateValidationCode() {
    // Generate a random 6-digit validation code
    Random random = new Random();
    int validationCode = 100000 + random.nextInt(900000);
    return String.valueOf(validationCode);
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

  public void resetPassword(String email, String validationCode, String newPassword) {
    // Retrieve user by email
    Optional<User> optionalUser = repository.findByEmail(email);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      // Check if the validation code matches
      if (user.getValidationCode() != null && user.getValidationCode().equals(validationCode)) {
        // Check if the code has expired (2 minutes)
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime codeTimestamp = user.getValidationCodeTimestamp();
        if (currentTime.minusMinutes(2).isBefore(codeTimestamp)) {
          // Code is valid and has not expired
          // Set the new password and clear the validation code and timestamp
          user.setPassword(passwordEncoder.encode(newPassword));
          user.setValidationCode(null);
          user.setValidationCodeTimestamp(null);
          repository.save(user);
        } else {
          throw new RuntimeException("Validation code has expired");
        }
      } else {
        throw new RuntimeException("Invalid validation code");
      }
    } else {
      throw new RuntimeException("User not found");
    }
  }
}
