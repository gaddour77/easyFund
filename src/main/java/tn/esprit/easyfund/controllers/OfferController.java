package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.OfferStatus;
import tn.esprit.easyfund.services.OfferServicesImpl;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/offre")
public class OfferController {

    private OfferServicesImpl offerServices;
    @PostMapping("/addOffre")
    public ResponseEntity<Offer> addOffre(@RequestBody Offer offer){
        System.out.println(offer.getOfferImage());
        Offer offer1 = offerServices.addOffer(offer);
        if (offer1!=null){
            return ResponseEntity.ok(offer1);
        }else {
            return ResponseEntity.notFound().build();
        }
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
    @PutMapping("/approve/{id}/{offerStatus}")
    public Offer approve(@PathVariable Long id,@PathVariable String offerStatus ){

        OfferStatus status =OfferStatus.valueOf(offerStatus);
        System.out.println(status);
        return offerServices.approve(id,status);
    }
    @GetMapping("/alloffers")
    public List<Offer> findAll(){
        return offerServices.findAll();
    }
    @GetMapping("/getbystatus/{status}")
    List<Offer> getByStatus(@PathVariable String status){
        List<Offer> statusList= offerServices.getByStatus(status);
        return  statusList;
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
    @DeleteMapping("/deletallOffers")
    public String deleteAll(){
        offerServices.deleteAll();
        return "database cleanned";
    }
}
