package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String accessToken;
  private String refreshToken;
  private String errorMessage;

  // Constructors...

  // Constructor for successful authentication without an error message
  public AuthenticationResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  // Constructor for authentication with an error message
  public AuthenticationResponse(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  // Getters and setters...

  // Builder pattern for creating instances
  public static AuthenticationResponseBuilder builder() {
    return new AuthenticationResponseBuilder();
  }

  // Builder class
  public static class AuthenticationResponseBuilder {

    private String accessToken;
    private String refreshToken;
    private String errorMessage;

    // Setters...

    // Set the access token
    public AuthenticationResponseBuilder accessToken(String accessToken) {
      this.accessToken = accessToken;
      return this;
    }

    // Set the refresh token
    public AuthenticationResponseBuilder refreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    // Set the error message
    public AuthenticationResponseBuilder errorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }

    // Build method to create the final AuthenticationResponse instance
    public AuthenticationResponse build() {
      if (errorMessage != null) {
        return new AuthenticationResponse(errorMessage);
      } else {
        return new AuthenticationResponse(accessToken, refreshToken);
      }
    }
  }
}

