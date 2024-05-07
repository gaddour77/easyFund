package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Claim;

import java.util.List;

public interface IClaimServices {
    Claim getClaimById(Long claimId);
    List<Claim> getAllPendingClaims();
    Claim saveClaim(Claim claim);
    Claim updateClaim(Long claimId, Claim updatedClaim);

    void deleteClaim(Long claimId);

    List<Claim> getClaimsAssignedToAgent();
    void takeClaim(Long claimId);
    void closeClaim(Long claimId);
    List<Claim> getAllUserClaims();



    //function
}
