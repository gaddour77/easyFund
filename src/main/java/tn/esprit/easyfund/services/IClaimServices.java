package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Claim;

import java.util.List;

public interface IClaimServices {
    Claim getClaimById(Long claimId);
    List<Claim> getAllClaims();
    Claim saveClaim(Claim claim);
    Claim updateClaim(Long claimId, Claim updatedClaim);

    void deleteClaim(Long claimId);
    Claim assignClaimToAgent(Claim claim);

    //function
}
