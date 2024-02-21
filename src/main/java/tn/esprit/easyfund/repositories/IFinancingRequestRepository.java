package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.FinancingRequest;

public interface IFinancingRequestRepository extends JpaRepository<FinancingRequest,Long> {
}
