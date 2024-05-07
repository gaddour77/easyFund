package tn.esprit.easyfund.controllers;

import io.jsonwebtoken.io.IOException;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.OfferCategory;
import tn.esprit.easyfund.entities.OfferStatus;
import tn.esprit.easyfund.services.OfferServicesImpl;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.util.Set;


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

    @PostMapping("/submitOffer")
    public ResponseEntity<Offer> submitOffer(@RequestParam("offerDescription") String offerDescription,
                                         @RequestParam("offerLink") String offerLink,
                                         @RequestParam("offerPrice") float offerPrice,
                                         @RequestParam("offerImage") MultipartFile offerImage,
                                         @RequestParam("offerStatus") String offerStatus,
                                         @RequestParam("offerCategory") String offerCategory) {

        // Créez votre objet Loan en utilisant les données reçues
        Offer loan = new Offer();
        loan.setOfferDescription(offerDescription);
        loan.setOfferLink(offerLink);
        loan.setOfferPrice(offerPrice);
        loan.setOfferStatus(OfferStatus.valueOf(offerStatus));
        loan.setOfferCategory(OfferCategory.valueOf(offerCategory));

        try {
            if (offerImage != null) {
                // Traitez le fichier d'image
                // loan.setOfferImage(offerImage); // Vous devrez écrire cette logique
                loan.setOfferImage(this.saveFile(offerImage,"offer"));
            }

            // Enregistrez l'objet Loan dans votre base de données ou effectuez toute autre opération nécessaire
            // loanService.saveLoan(loan);

            // Retournez une réponse appropriée
            return ResponseEntity.ok(offerServices.addOffer(loan));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(offerServices.addOffer(loan));
        }
    }
    public String saveFile(MultipartFile file, String prefix) throws IOException {
        String destinationDirectory="C:/xampp/htdocs/easyFund/img";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = generateUniqueFileName(prefix, extension);
        File destFile = new File(destinationDirectory + File.separator + newFileName);
        try {
            file.transferTo(destFile);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return destinationDirectory+"/"+newFileName;
    }

    private String generateUniqueFileName(String prefix, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        return prefix + "_" + formattedDateTime + extension;
    }

}

