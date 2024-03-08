package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.Role;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IClaimRepository;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;

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
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
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

        // Assign the claim to an agent
        assignClaimToAgent(claim);

        // Save the claim
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
    public Claim assignClaimToAgent(Claim claim) {
        // Find the agent with the least open claims or the agent with the smallest ID if none have open claims
        User agentWithLeastOpenClaims = findAgentWithLeastOpenClaims()
                .orElse(findAgentWithSmallestId());

        // Assign the claim to the agent
        claim.setAgent(agentWithLeastOpenClaims);

        // Save the updated claim
        return claimRepository.save(claim);
    }

    private Optional<User> findAgentWithLeastOpenClaims() {
        return Optional.ofNullable(userRepository.findAgentWithLeastOpenClaims());
    }

    private User findAgentWithSmallestId() {
        // Find the agent with the smallest ID
        return userRepository.findTopByRoleOrderByUserIdAsc(Role.AGENT)
                .orElseThrow(() -> new RuntimeException("No agents found"));
    }
}
