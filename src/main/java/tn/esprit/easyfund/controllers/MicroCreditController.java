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


@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "credit")
@RestController
public class MicroCreditController {

    @Autowired
    private MicroCreditServicesImpl microCreditService;

    private static final Logger log = LoggerFactory.getLogger(MicroCreditController.class);

    @PostMapping(path = "createCredit")
    public ResponseEntity<MicroCredit> createMicroCredit(@RequestBody MicroCredit microCredit) {
        MicroCredit credit = microCreditService.createMicroCredit(microCredit);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getCredit/{id}")
    public ResponseEntity<MicroCredit> getCreditById(@PathVariable("id") long id) {
        MicroCredit credit = microCreditService.findCreditById(id);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getAllCredits")
    public ResponseEntity<List<MicroCredit>> getAllcredits() {
        List<MicroCredit> credits = microCreditService.findAllCredits();
        if (credits != null) {
            return ResponseEntity.ok(credits);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("updateCredit/{idCredit}")
    public ResponseEntity<MicroCredit> updateCredit(@PathVariable Long idCredit) {
        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("deleteCredit/{id}")
    public ResponseEntity<?> deleteCredit(@PathVariable("id") Long id) {
            microCreditService.deleteCredit(id);
            return null;
    }

    @GetMapping("getCreditsByStatus/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByStatus(@PathVariable("status") CreditStatus creditStatus) {
        List<MicroCredit> credits = microCreditService.getCreditsByStatus(creditStatus);
        if (credits != null) {
            return ResponseEntity.ok(credits);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("getCreditsByType/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByType(@PathVariable("status") CreditType creditType) {
        List<MicroCredit> credits = microCreditService.getCreditsByType(creditType);
        if (credits != null) {
            return ResponseEntity.ok(credits);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("updateCreditStatus/{idCredit}/{status}")
    public ResponseEntity<MicroCredit> updateCreditStatus(@PathVariable("idCredit") Long idCredit, @PathVariable("status") CreditStatus status) {
        MicroCredit updatedCredit = microCreditService.updateStatus(idCredit, status);
        if (updatedCredit != null) {
            return ResponseEntity.ok(updatedCredit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("getCreditByAccount/{idAccount}")
    public ResponseEntity<List<MicroCredit>> getCreditByAccount(@PathVariable("idAccount") Long id) {
        List<MicroCredit> credits = microCreditService.getCreditByAccountId(id);
        if (credits != null) {
            return ResponseEntity.ok(credits);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}


