package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.easyfund.entities.FinancingRequest;

import java.util.List;

public interface IFinancingRequestRepository extends JpaRepository<FinancingRequest,Long> {
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.offer.offreId = :offerId")
    List<FinancingRequest> findByOffer(@Param("offerId") Long offerId);
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.user.userId = :userId")
    List<FinancingRequest> findByUser(@Param("userId") Long userId);
}
