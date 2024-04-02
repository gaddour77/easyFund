package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.services.BankAccService;


import java.util.List;

@RestController
public class BankAccController {
    @Autowired
    private BankAccService service;

    @PostMapping("/addBankAcc/{userId}")
    public BankAccount addBankAcc( @RequestBody BankAccount bankAccount,@PathVariable Long userId) {
        return service.saveAccount(userId, bankAccount);
    }


    @GetMapping("/getAccounts")
    public List<BankAccount> getAccounts() {
        return service.getAccounts();
    }

    @GetMapping("/getAccountId/{id}")
    public BankAccount getAccountId(@PathVariable Long id) {
        return service.getAccountId(id);
    }

    @PutMapping("/putBankAcc")
    public BankAccount putBankAcc(@RequestBody BankAccount bankAccount) {
        return service.updateAccount(bankAccount);
    }

    @DeleteMapping("/deleteBankAcc/{id}")
    public String deleteBankAcc(@PathVariable Long id) {
        return service.deleteAccount(id);
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateAccount(@PathVariable Long id) {
        String result = service.deactivateAccount(id);
        if (result.startsWith("Account with ID")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateAccount(@PathVariable Long id) {
        String result = service.activateAccount(id);
        if (result.startsWith("Account with ID")) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/verifyDeposit/{transactionId}")
    public void verifyDepositTransaction(@PathVariable Long transactionId, @RequestParam String status) {
        service.verifyDepositTransaction(transactionId, status);
    }
    @PostMapping("/verifyWithdraw/{transactionId}")
    public void verifyWithdrawTransaction(@PathVariable Long transactionId, @RequestParam String status) {
        service.verifyWithdrawTransaction(transactionId, status);
    }
    @PostMapping("/verifyPerfectTransaction/{transactionId}")
    public void verifyPerfectTransaction(@PathVariable Long transactionId, @RequestParam String status) {
        service.verifyPerfectTransaction(transactionId, status);
    }

    @PostMapping("/updateAccountScore/{accountId}")
    public void updateAccountScore(@PathVariable Long accountId) {
        // Call the service method to update the account score
        service.updateAccountScore(accountId);
    }

}
