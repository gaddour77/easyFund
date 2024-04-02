package tn.esprit.easyfund.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.InsuranceContract;
import tn.esprit.easyfund.entities.User;



public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, Long> {
    List<InsuranceContract> findByAgent(User agent);
}