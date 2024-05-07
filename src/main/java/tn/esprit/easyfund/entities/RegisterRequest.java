package tn.esprit.easyfund.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
  private LocalDate DateNaissance ;
  private String phoneNumber;
  private Long cin ;
  private Float salary ;

}
