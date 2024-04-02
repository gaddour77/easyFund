package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.entities.Transaction;


import java.util.List;

public interface TransactionRep extends JpaRepository<Transaction,Long > {
    List<Transaction> findByBankAccount(BankAccount bankAccount);

    @Query("SELECT t FROM Transaction t WHERE t.bankAccount.id = :accountId")
    List<Transaction> findByBankAccountId(@Param("accountId") Long accountId);


}
