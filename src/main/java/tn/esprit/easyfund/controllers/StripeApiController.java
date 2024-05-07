package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.dto.StripeChargeDto;
import tn.esprit.easyfund.dto.StripeTokenDto;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.services.FinancingRequestServicesImpl;
import tn.esprit.easyfund.services.StripeService;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/stripe")
public class StripeApiController {
    private final StripeService stripeService;
    private final FinancingRequestServicesImpl financingRequestServices;
    private IFinancingRequestRepository financingRequestRepository;


    @PostMapping("/card/token")
    @ResponseBody
    public StripeTokenDto createCardToken(@RequestBody StripeTokenDto model) {


        return stripeService.creatCardToken(model);
    }

    @PostMapping("/charge/{id}")
    @ResponseBody
    public StripeChargeDto charge(@RequestBody StripeChargeDto model,@PathVariable long id) throws IOException {

       // financingRequestServices.installmentPayment(id);


         String token = "tok_visa";
         model.setStripeToken(token);

        financingRequestServices.installmentPayment(id);
        FinancingRequest financingRequest = financingRequestServices.findById(id);
        double amount = financingRequestServices.installmentPayment(id);
         model.setAmount(amount);

        return stripeService.charge(model);
    }


}
