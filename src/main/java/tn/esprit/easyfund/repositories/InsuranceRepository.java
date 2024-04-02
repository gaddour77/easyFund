package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Insurance;



public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    
}