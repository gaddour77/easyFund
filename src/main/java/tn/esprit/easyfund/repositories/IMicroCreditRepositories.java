package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;


public interface IMicroCreditRepositories extends JpaRepository<MicroCredit,Long> {

}
