package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.services.IClaimServices;

import java.util.List;


    @RestController
    @RequestMapping("/api/claims")
    public class ClaimController {

        @Autowired
        private IClaimServices claimService;

        @GetMapping("/{claimId}")
        public ResponseEntity<Claim> getClaimById(@PathVariable Long claimId) {
            Claim claim = claimService.getClaimById(claimId);
            return ResponseEntity.ok(claim);
        }

        @GetMapping
        public ResponseEntity<List<Claim>> getAllClaims() {
            List<Claim> claims = claimService.getAllClaims();
            return ResponseEntity.ok(claims);
        }

        @PostMapping
        public ResponseEntity<Claim> saveClaim(@RequestBody Claim claim) {
            Claim savedClaim = claimService.saveClaim(claim);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClaim);
        }
        @PutMapping("/{claimId}")
        public ResponseEntity<Claim> updateClaim(@PathVariable Long claimId, @RequestBody Claim updatedClaim) {
            Claim updatedClaimData = claimService.updateClaim(claimId, updatedClaim);

            if (updatedClaimData != null) {
                return ResponseEntity.ok(updatedClaimData);
            } else {
                // Handle claim not found
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{claimId}")
        public ResponseEntity<Void> deleteClaim(@PathVariable Long claimId) {
            claimService.deleteClaim(claimId);
            return ResponseEntity.noContent().build();
        }
    }


