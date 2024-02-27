package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.BankAccount;


public interface BankAccountRep extends JpaRepository<BankAccount,Long> {
}
