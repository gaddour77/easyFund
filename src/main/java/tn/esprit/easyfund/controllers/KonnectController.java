package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.easyfund.dto.PaymentRequest;
import tn.esprit.easyfund.services.KonnectService;
@AllArgsConstructor
@RestController
@RequestMapping("/konnect")
public class KonnectController {

    private KonnectService konnectService;

    @PostMapping("/initiate")
    public String initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        return konnectService.initiatePayment(
                paymentRequest.getAmount(),
                paymentRequest.getToken(),
                paymentRequest.getDescription());
    }
}
