package tn.esprit.easyfund.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Generates a no-argument constructor
public class PaymentRequest {

    private String receiverWalletId;
    private String token; // Currency
    private double amount;
    private String type;
    private String description;
    private List<String> acceptedPaymentMethods;
    private int lifespan;
    private boolean checkoutForm;
    private boolean addPaymentFeesToAmount;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String orderId;
    private String webhook;
    private boolean silentWebhook;
    private String successUrl;
    private String failUrl;
    private String theme;
}

