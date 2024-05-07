package tn.esprit.easyfund.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.AuthenticationRequest;
import tn.esprit.easyfund.entities.AuthenticationResponse;
import tn.esprit.easyfund.entities.RegisterRequest;
import tn.esprit.easyfund.entities.ValidationMethod;
import tn.esprit.easyfund.services.AuthenticationService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app


public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
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


  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
  @PostMapping("/forgot-password/send-code")
  public ResponseEntity<Void> sendValidationCode(@RequestBody PasswordResetRequest request) {
    String email = request.getEmail();
    ValidationMethod validationMethod = request.getValidationMethod();

    // Call your service with the provided email and validation method
    service.sendValidationCode(email, validationMethod);
    return ResponseEntity.ok().build();
  }

  // DTO for the incoming request
  public static class PasswordResetRequest {
    private String email;
    private ValidationMethod validationMethod;

    // Getters and Setters
    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public ValidationMethod getValidationMethod() {
      return validationMethod;
    }

    public void setValidationMethod(ValidationMethod validationMethod) {
      this.validationMethod = validationMethod;
    }
  }

  @PostMapping("/forgot-password/reset")
  public ResponseEntity<String> resetPassword(@RequestBody PasswordResetReq request) {
    String email = request.getEmail();
    String validationCode = request.getValidationCode();
    String newPassword = request.getNewPassword();

    // Call your service with the provided parameters
    service.resetPassword(email, validationCode, newPassword);
    return ResponseEntity.ok("Password reset successful");
  }

  // DTO for the incoming request
  public static class PasswordResetReq {
    private String email;
    private String validationCode;
    private String newPassword;

    // Getters and Setters
    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getValidationCode() {
      return validationCode;
    }

    public void setValidationCode(String validationCode) {
      this.validationCode = validationCode;
    }

    public String getNewPassword() {
      return newPassword;
    }

    public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
    }
  }


}
