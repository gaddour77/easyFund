package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;

import java.util.List;

@Repository
public interface IMicroCreditRepositories extends JpaRepository<MicroCredit,Long> {

    @Query("SELECT credit from MicroCredit credit where credit.creditType = ?1")
    List<MicroCredit> retrieveCreditsByType(CreditType type);

    @Query("SELECT credit from MicroCredit credit where credit.creditStatus = ?1")
    List<MicroCredit> retrieveCreditsByStatus(CreditStatus status);

}
