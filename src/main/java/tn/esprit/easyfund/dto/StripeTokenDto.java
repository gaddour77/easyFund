package tn.esprit.easyfund.dto;

import lombok.Data;
@Data
public class StripeTokenDto {
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
    private String token;
    private String username;
    private boolean succes;
}
