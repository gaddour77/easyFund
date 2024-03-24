package tn.esprit.easyfund.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.AuthenticationRequest;
import tn.esprit.easyfund.entities.AuthenticationResponse;
import tn.esprit.easyfund.entities.ValidationMethod;
import tn.esprit.easyfund.services.AuthenticationService;
import tn.esprit.easyfund.entities.RegisterRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
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
  public ResponseEntity<Void> sendValidationCode(
          @RequestParam(value = "email") String email,
          @RequestParam(value = "validationMethod") ValidationMethod validationMethod
  ) {
    service.sendValidationCode(email, validationMethod);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/forgot-password/verify-code")
  public ResponseEntity<Void> verifyValidationCode(
          @RequestParam (value = "email") String email,
          @RequestParam (value = "validationCode")String validationCode
  ) {
    boolean isValid = service.verifyValidationCode(email, validationCode);
    if (isValid) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/forgot-password/reset-password")
  public ResponseEntity<Void> resetPassword(
          @RequestParam String email,
          @RequestParam String newPassword
  ) {
    service.resetPassword(email, newPassword);
    return ResponseEntity.ok().build();
  }


}
