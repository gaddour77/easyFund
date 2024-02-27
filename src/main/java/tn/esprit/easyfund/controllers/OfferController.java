package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.services.implementations.OfferServicesImpl;

@RestController
@AllArgsConstructor
@RequestMapping("/offre")
public class OfferController {

    private OfferServicesImpl offerServices;
    @PostMapping("/addOffre")
    public Offer addOffre(@RequestBody Offer offer){
        return offerServices.addOffer(offer);
    }
    @GetMapping("/findOffer/{id}")
    public Offer findById(@PathVariable long id){
        return offerServices.findById(id);
    }

}
