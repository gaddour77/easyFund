package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.ClaimStatus;
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
    public List<Claim> getAllOpenClaims() {
        return claimRepository.getAllOpenClaimsByClaimStatus(ClaimStatus.OPEN);
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
        claim.setClaimStatus(ClaimStatus.OPEN);

        // Assign the claim to an agent
        return assignClaimToAgent(claim);


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
        // Find the agent with the least open claims
        User agentWithLeastOpenClaims = findAgentWithLeastOpenClaims()
                .orElse(findAgentWithSmallestId());

        // If there are multiple agents with the same number of open claims, choose the one with the smallest ID
        if (agentWithLeastOpenClaims != null) {
            User agentWithSmallestId = findAgentWithSmallestId().orElse(null);

            if (agentWithSmallestId != null) {
                // Compare the counts and IDs
                long leastOpenClaimsCount = countOpenClaims(agentWithLeastOpenClaims);
                long smallestIdClaimsCount = countOpenClaims(agentWithSmallestId);

                if (leastOpenClaimsCount < smallestIdClaimsCount) {
                    claim.setAgent(agentWithLeastOpenClaims);
                } else {
                    claim.setAgent(agentWithSmallestId);
                }
            } else {
                claim.setAgent(agentWithLeastOpenClaims);
            }
        }

        // Save the updated claim
        return claimRepository.save(claim);
    }

    private long countOpenClaims(User agent) {
        // Count the number of open claims for a specific agent
        return claimRepository.countByAgentAndClaimStatus(agent, ClaimStatus.OPEN);
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
            return claimRepository.findByAgent(agent);
        } else {
            // Handle the case where the user is not authenticated or has an invalid principal
            throw new RuntimeException("Invalid authentication or principal");
        }
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
