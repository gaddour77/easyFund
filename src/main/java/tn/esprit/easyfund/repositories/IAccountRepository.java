package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Account;


public interface IAccountRepository extends JpaRepository<Account,Long> {

}