package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Claim;

public interface IClaimRepository extends JpaRepository<Claim, Long> {
}