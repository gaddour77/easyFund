package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Transaction;


import java.util.List;

public interface TransactionRep extends JpaRepository<Transaction,Long > {

}
