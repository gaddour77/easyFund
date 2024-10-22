package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.services.IClaimServices;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app

    @RequestMapping("/api/claims")
    public class ClaimController {

        @Autowired
        private IClaimServices claimService;
    @Autowired
    private IUserRepository userRepository;

        @GetMapping("/{claimId}")
        public ResponseEntity<Claim> getClaimById(@PathVariable Long claimId) {
            Claim claim = claimService.getClaimById(claimId);
            return ResponseEntity.ok(claim);
        }

        @GetMapping
        public ResponseEntity<List<Claim>> getAllOpenClaims() {
            List<Claim> claims = claimService.getAllPendingClaims();
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

        @GetMapping("/assigned-to-agent")
        public List<Claim> getClaimsAssignedToAgent() {
            try {
                // Call the service method to get claims assigned to the agent
                return claimService.getClaimsAssignedToAgent();
            } catch (Exception e) {
                // Handle exceptions and return an appropriate response
                return (List<Claim>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    @PostMapping("/{claimId}/take")
    public ResponseEntity<String> takeClaim(@PathVariable Long claimId) {
        try {
            // Call the service method to allow the agent to take the claim
            claimService.takeClaim(claimId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/{claimId}/close")
    public ResponseEntity<String> closeClaim(@PathVariable Long claimId) {
        try {
            // Call the service method to allow the agent to take the claim
            claimService.closeClaim(claimId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/assigned-to-user")
    public List<Claim> getClaimsAssignedToUser() {
        try {
            // Call the service method to get claims assigned to the agent
            return claimService.getAllUserClaims();
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return (List<Claim>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    }


