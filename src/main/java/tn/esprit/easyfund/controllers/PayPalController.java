package tn.esprit.easyfund.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import org.json.*;


@RestController
@RequestMapping(value = "/paypal")
public class PayPalController {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final String BASE = "https://api-m.sandbox.paypal.com";

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        System.out.println(Base64.getEncoder().encodeToString(auth.getBytes()));
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String generateAccessToken() {
        String auth = this.getAuth(
                "AducQ8bYxqjeAl0C2aGOWi19PJ_bE1rFOXyPma24VH_grkfKmoOmTv3lEHg884Dq1Y5ekyKprB7zgIyq",
                "EC8oEJqKvgmtA_hmWalznBjmr-y6Qvt06hptWPC8q83w-LwKdLX6EXQhnIjnvVPiKQwqcg6tgcH2l6mB"
        );
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE + "/v1/oauth2/token",
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.log(Level.INFO, "GET TOKEN: SUCCESSFUL!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.log(Level.SEVERE, "GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }

    @RequestMapping(value = "/api/orders/{orderId}/capture", method = RequestMethod.POST)
    @CrossOrigin
    public Object capturePayment(@PathVariable("orderId") String orderId) {
        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }

    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    @CrossOrigin
    public Object createOrder() {
//        public Object createOrder(double amount) {
        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create JSON request with parameters
//        String requestJson = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\" USD\",\"value\":" + amount + "}}]}";
        String requestJson = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":100}}]}";

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CAPTURE");
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CAPTURING ORDER");
            return "Unavailable to get CAPTURE ORDER, STATUS CODE " + response.getStatusCode();
        }
    }
}
