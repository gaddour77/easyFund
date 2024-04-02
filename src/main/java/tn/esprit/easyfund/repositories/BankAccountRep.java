package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.easyfund.entities.BankAccount;


public interface BankAccountRep extends JpaRepository<BankAccount,Long> {
    @Query("SELECT ba FROM BankAccount ba WHERE ba.count_ref = :countRef")
    BankAccount findByCount_ref(String countRef);
}
