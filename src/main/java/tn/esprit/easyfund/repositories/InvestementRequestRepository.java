package tn.esprit.easyfund.repositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.easyfund.entities.InvestementRequest;

@Repository
public interface InvestementRequestRepository extends JpaRepository<InvestementRequest, Integer>
{
}
