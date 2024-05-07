package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.RequestStatus;

import tn.esprit.easyfund.entities.User;


import java.util.List;

public interface IFinancingRequestRepository extends JpaRepository<FinancingRequest,Long> {
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.offer.offreId = :offerId")
    List<FinancingRequest> findByOffer(@Param("offerId") Long offerId);
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.user.userId = :userId")
    List<FinancingRequest> findByUser(@Param("userId") Long userId);
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.requestStatus = :status")
    List<FinancingRequest> findByRequestStatus(@Param("status")RequestStatus requestStatus);
    @Query("SELECT fr FROM FinancingRequest fr WHERE fr.user.userId =:user and fr.offer.offreId =:offer")
     List<FinancingRequest>  findByUserIdAndOfferId(@Param("user")Long idu ,@Param("offer")Long ido);

    @Query("SELECT fr.user FROM FinancingRequest fr WHERE fr.financingRequestId = :financingRequestId")
    User findUserByFinancingRequestId(@Param("financingRequestId")Long financingRequestId);


}
