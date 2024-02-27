package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.services.BankAccService;


import java.util.List;

@RestController
public class BankAccController {
    @Autowired
    private BankAccService service;

    @PostMapping("/addBankAcc")
    public BankAccount addBankAcc(@RequestBody BankAccount bankAccount){
        return service.saveAccount(bankAccount);
    }

    @GetMapping("/getAccounts")
    public List<BankAccount> getAccounts(){
        return service.getAccounts();
    }
    @GetMapping("/getAccountId/{id}")
    public BankAccount getAccountId(@PathVariable  Long id){
        return service.getAccountId(id);
    }
    @PutMapping("/putBankAcc")
    public BankAccount putBankAcc(@RequestBody BankAccount bankAccount){
        return service.updateAccount(bankAccount);
    }
    @DeleteMapping("/deleteBankAcc/{id}")
    public String deleteBankAcc(@PathVariable Long id){
        return service.deleteAccount(id);
    }
}
