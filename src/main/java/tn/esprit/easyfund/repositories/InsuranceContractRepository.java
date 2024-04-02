package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.InsuranceContract;



public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, Long> {
    
}