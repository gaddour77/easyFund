package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;

import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.exceptions.MicroCreditNotFoundException;
import tn.esprit.easyfund.services.IMicroCreditServicesImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "credit")
@RestController
public class MicroCreditController {

    @Autowired
    private IMicroCreditServicesImpl microCreditService;

    private static final Logger log = LoggerFactory.getLogger(MicroCreditController.class);

    @PostMapping(path = "/createCredit")
    public ResponseEntity<MicroCredit> createMicroCredit(@RequestBody MicroCredit microCredit) {
        try {
            MicroCredit savedCredit = microCreditService.createMicroCredit(microCredit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCredit); // Return 201 Created with saved credit
        } catch (MicroCreditAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return 409 Conflict
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("getCredit/{id}")
    public MicroCredit getCreditById(@PathVariable("id") long id) {
        return microCreditService.findCreditById(id);
    }

    @GetMapping("getAllCredits")
    public List<MicroCredit> getAllcredits() {
        return microCreditService.findAllCredits();
    }

    @PutMapping("updateCredit")
    public ResponseEntity<MicroCredit> updateCredit(@RequestBody MicroCredit credit) {
        try {
            MicroCredit updatedCredit = microCreditService.updateCredit(credit);
            return ResponseEntity.ok(updatedCredit); // Return 200 OK with updated credit
        } catch (MicroCreditNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if credit not found
        } catch (Exception e) {
            log.error("Error updating credit", e); // Log unexpected errors
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }
    @DeleteMapping("deleteCredit/{id}")
    public ResponseEntity<?> deleteCredit(@PathVariable("id") Long id) {
        try {
            microCreditService.deleteCredit(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on success
        } catch (MicroCreditNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if credit not found
        } catch (Exception e) {
            log.error("Error deleting credit", e); // Log unexpected errors
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }

    @GetMapping("getCreditsByStatus/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByStatus(
            @PathVariable("status") CreditStatus creditStatus) {
        try {
            List<MicroCredit> credits = microCreditService.getCreditsByStatus(creditStatus);
            if (credits.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found if no credits found
            }
            return ResponseEntity.ok(credits); // Return 200 OK with list of credits
        } catch (Exception e) {
            log.error("Error retrieving credits by status", e); // Log unexpected errors
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }

    @GetMapping("getCreditsByType/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByType(
            @PathVariable("status") CreditType creditType) {
        try {
            List<MicroCredit> credits = microCreditService.getCreditsByType(creditType);
            if (credits.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 Not Found if no credits found
            }
            return ResponseEntity.ok(credits); // Return 200 OK with list of credits
        } catch (Exception e) {
            log.error("Error retrieving credits by type", e); // Log unexpected errors
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }

    @PutMapping("updateCreditStatus/{idCredit}/{status}")
    public ResponseEntity<MicroCredit> updateCreditStatus(@PathVariable("idCredit") Long idCredit, @PathVariable("status") CreditStatus status)
    {
        try {
            MicroCredit updatedCredit = microCreditService.updateStatus(idCredit, status);
            return ResponseEntity.ok(updatedCredit); // Return 200 OK with updated credit
        } catch (MicroCreditNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        } catch (Exception e) {
            log.error("Error updating credit status", e); // Log unexpected errors
            return ResponseEntity.internalServerError().build(); // Return 500 Internal Server Error
        }
    }
}


