package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.repositories.IClaimRepository;

import java.util.List;

@Service
public class ClaimServicesImpl implements IClaimServices{
    @Autowired
    private IClaimRepository claimRepository;

    @Override
    public Claim getClaimById(Long claimId) {
        return claimRepository.findById(claimId).orElse(null);
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    @Override
    public Claim saveClaim(Claim claim) {
        // Add any additional business logic if needed
        return claimRepository.save(claim);
    }
    @Override
    public Claim updateClaim(Long claimId, Claim updatedClaim) {
        Claim existingClaim = claimRepository.findById(claimId).orElse(null);

        if (existingClaim != null) {
            // Update fields that are allowed to be updated
            existingClaim.setDescription(updatedClaim.getDescription());
            existingClaim.setClaimType(updatedClaim.getClaimType());
            existingClaim.setClaimStatus(updatedClaim.getClaimStatus());
            existingClaim.setUser(updatedClaim.getUser());
            // Update other fields as needed

            return claimRepository.save(existingClaim);
        } else {
            // Handle claim not found
            return null;
        }
    }
    @Override
    public void deleteClaim(Long claimId) {
        claimRepository.deleteById(claimId);
    }

}
