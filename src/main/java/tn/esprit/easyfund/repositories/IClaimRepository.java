package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.ClaimStatus;
import tn.esprit.easyfund.entities.User;

import java.util.List;

public interface IClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> getAllOpenClaimsByClaimStatus(ClaimStatus claimStatus);

    List<Claim> findByAgent(User agent);

    long countByAgentAndClaimStatus(User agent, ClaimStatus claimStatus);
}