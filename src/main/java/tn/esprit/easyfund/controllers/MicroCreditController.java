package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;

import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.services.MicroCreditServicesImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "credit")
@RestController
public class MicroCreditController {

    @Autowired
    private MicroCreditServicesImpl microCreditService;

    @PostMapping(path = "createCredit")
    public ResponseEntity<MicroCredit> createMicroCredit(@RequestBody MicroCredit microCredit) {
        MicroCredit credit = microCreditService.createMicroCredit(microCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credit); // Created return 201
    }

    @GetMapping("getCredit/{id}")
    public ResponseEntity<MicroCredit> getCreditById(@PathVariable("id") long id) {
        MicroCredit credit = microCreditService.findCreditById(id);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credit); // get successfully return 200
    }

    @GetMapping("getAllCredits")
    public ResponseEntity<List<MicroCredit>> getAllCredits() {
        List<MicroCredit> credits = microCreditService.findAllCredits();
        if (credits == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credits);
    }

    @DeleteMapping("deleteCredit/{id}")
    public ResponseEntity<?> deleteCredit(@PathVariable("id") Long idCredit) {

        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        microCreditService.deleteCredit(idCredit);
        return ResponseEntity.ok().build(); // Credit deleted successfully, return 200
    }

    //
    @PutMapping("updateCredit/{idCredit}")
    public ResponseEntity<MicroCredit> updateCredit(@PathVariable Long idCredit, @RequestBody MicroCredit updatedCredit) {
        if (!idCredit.equals(updatedCredit.getMicroCreditId())) {
            return ResponseEntity.badRequest().build();
        }
        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Updated deleted successfully, return 200
        }
        MicroCredit updatedCreditEntity = microCreditService.updateCredit(updatedCredit);
        return ResponseEntity.ok(updatedCreditEntity);
    }

    @GetMapping("getCreditsByStatus/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByStatus(@PathVariable("status") CreditStatus creditStatus) {
        List<MicroCredit> credits = microCreditService.getCreditsByStatus(creditStatus);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200

    }

    @GetMapping("getCreditsByType/{type}")
    public ResponseEntity<List<MicroCredit>> getCreditsByType(@PathVariable("type") CreditType creditType) {
        List<MicroCredit> credits = microCreditService.getCreditsByType(creditType);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200
    }

    @PutMapping("updateCreditStatus/{idCredit}/{status}")
    public ResponseEntity<MicroCredit> updateCreditStatus(@PathVariable("idCredit") Long idCredit, @PathVariable("status") CreditStatus status) {
        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }
        MicroCredit updatedCredit = microCreditService.updateStatus(idCredit, status); // Credit updated successfully, return 200
        return ResponseEntity.ok(updatedCredit);
    }

    @GetMapping("getCreditByAccount/{idAccount}")
    public ResponseEntity<List<MicroCredit>> getCreditByAccount(@PathVariable("idAccount") Long id) {
        List<MicroCredit> credits = microCreditService.getCreditByAccountId(id);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200
    }


    //////////////////////////////////////////////////////////////////



}


