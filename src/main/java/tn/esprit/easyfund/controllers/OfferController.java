package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.services.OfferServicesImpl;

import java.util.List;
import java.util.Set;

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
    @PostMapping("/addoffers")
    public List<Offer> addOffers(@RequestBody List<Offer> listOffers)
    {
        return offerServices.addOffers(listOffers);
    }
    @PutMapping("/update")
    public Offer updateOffer(@RequestBody Offer offer){
        return offerServices.updateOffer(offer);
    }
    @GetMapping("/alloffers")
    public List<Offer> findAll(){
        return offerServices.findAll();
    }
    @DeleteMapping("/delete/{id}")
    public String delete (@PathVariable long id){
        return offerServices.delete(id);
    }
    @PostMapping("/scrap")
    public List<Offer> scrap(){return offerServices.addScrap();}
    @PutMapping("/affectation/{ido}/{idf}")
    public FinancingRequest affectation(@PathVariable Long ido,@PathVariable Long idf){
        return offerServices.affecter(ido, idf);
    }
    @GetMapping("/getfinancing/{id}")
    public List<FinancingRequest> financingRequests(@PathVariable Long id){
        return offerServices.financingRequests(id);
    }
}
