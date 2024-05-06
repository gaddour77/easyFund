package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.ClaimStatus;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IClaimRepository;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClaimServicesImpl implements IClaimServices{
    @Autowired
    private IClaimRepository claimRepository;
    @Autowired
    private IUserRepository userRepository;

    @Override
    public Claim getClaimById(Long claimId) {
        return claimRepository.findById(claimId).orElse(null);
    }

    @Override
    public List<Claim> getAllPendingClaims() {
        return claimRepository.getAllOpenClaimsByClaimStatus(ClaimStatus.PENDING);
    }

    @Override
    public Claim saveClaim(Claim claim) {
        // Add any additional business logic if needed

        // Get the current user details from the security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Find the user based on the username (assuming username is the email)
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Set the user for the claim
        claim.setUser(user);
        claim.setClaimStatus(ClaimStatus.PENDING);

        // Assign the claim to an agent
        return claimRepository.save(claim);


    }
    @Override
    public Claim updateClaim(Long claimId, Claim updatedClaim) {
        Claim existingClaim = claimRepository.findById(claimId).orElse(null);

        if (existingClaim != null) {

            existingClaim.setDescription(updatedClaim.getDescription());
            existingClaim.setClaimType(updatedClaim.getClaimType());
            existingClaim.setClaimStatus(updatedClaim.getClaimStatus());
            existingClaim.setUser(updatedClaim.getUser());


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

    @Override
    public List<Claim> getClaimsAssignedToAgent() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the user is authenticated and has a valid principal
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Retrieve the currently authenticated agent
            User agent = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            // Retrieve claims assigned to the agent
            return claimRepository.findByAgentAndClaimStatus(agent,ClaimStatus.OPEN);
        } else {
            // Handle the case where the user is not authenticated or has an invalid principal
            throw new RuntimeException("Invalid authentication or principal");
        }
    }
    @Override
    public void takeClaim(Long claimId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Find the user based on the username (assuming username is the email)
        User agent = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            Claim claim = claimRepository.findById(claimId)
                    .orElseThrow(() -> new NoSuchElementException("Claim not found"));

            if (claim.getClaimStatus() == ClaimStatus.PENDING) {
                claim.setClaimStatus(ClaimStatus.OPEN);
                claim.setAgent(agent);
                claimRepository.save(claim);
            } else {
                throw new RuntimeException("Claim is not pending");
            }
        }

    @Override
    public void closeClaim(Long claimId) {


        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NoSuchElementException("Claim not found"));


            claim.setClaimStatus(ClaimStatus.CLOSED);

            claimRepository.save(claim);

    }

    @Override
    public List<Claim> getAllUserClaims() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Find the user based on the username (assuming username is the email)
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return claimRepository.findByUser(user);    }
}


