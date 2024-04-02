package tn.esprit.easyfund.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor

public class KonnectService {

    private final RestTemplate restTemplate = new RestTemplate();


    private final String apiKey;


    private final String walletId;




    public String initiatePayment(double amount, String currency, String description) {
        String url = "https://api.konnect.network/api/v2/konnect-gateway/payments/init-payment"; // Use the correct endpoint

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("receiverWalletId", walletId);
        requestBody.put("amount", amount);
        requestBody.put("token", currency);
        requestBody.put("description", description);


        HttpHeaders headers = new HttpHeaders();

        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody(); // This should include the payment link
    }
}
