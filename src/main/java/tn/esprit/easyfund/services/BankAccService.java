package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.repositories.BankAccountRep;


import java.util.List;

@Service
public class BankAccService {
    @Autowired
    private BankAccountRep repository;

    public BankAccount saveAccount(BankAccount bankaccount){
        return repository.save(bankaccount);
    }
    public List<BankAccount> getAccounts(){
        return  repository.findAll();
    }

    public BankAccount getAccountId(Long id){
        return repository.findById(id).orElse(null);

    }
    public String deleteAccount(Long id){
        repository.deleteById(id);
        return "banck account removed "+id;
    }
    public BankAccount updateAccount(BankAccount bankAccount){
        BankAccount existingAccount=repository.findById(bankAccount.getAccount_id()).orElse(null);
        existingAccount.setBalance(bankAccount.getBalance());
        existingAccount.setScore(bankAccount.getScore());
        existingAccount.set_active(bankAccount.is_active());
        existingAccount.setUpdate_date(bankAccount.getUpdate_date());
        return repository.save(existingAccount);
    }

}
