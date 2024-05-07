package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Account;
import tn.esprit.easyfund.services.AccountService;

import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private AccountService service;

    @PostMapping("/addBankAcc/{userId}")
    public Account addBankAcc( @RequestBody Account Account,@PathVariable Long userId) {
        return service.saveAccount(userId, Account);
    }


    @GetMapping("/getAccounts")
    public List<Account> getAccounts() {
        return service.getAccounts();
    }

    @GetMapping("/getAccountId/{id}")
    public Account getAccountId(@PathVariable Long id) {
        return service.getAccountId(id);
    }

    @PutMapping("/putBankAcc")
    public Account putBankAcc(@RequestBody Account Account) {
        return service.updateAccount(Account);
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

//    @PostMapping("/verifyDeposit/{transactionId}")
//    public void verifyDepositTransaction(@PathVariable Long transactionId, @RequestParam String status) {
//        service.verifyDepositTransaction(transactionId, status);
//    }
//    @PostMapping("/verifyWithdraw/{transactionId}")
//    public void verifyWithdrawTransaction(@PathVariable Long transactionId, @RequestParam String status) {
//        service.verifyWithdrawTransaction(transactionId, status);
//    }
//    @PostMapping("/verifyPerfectTransaction/{transactionId}")
//    public void verifyPerfectTransaction(@PathVariable Long transactionId, @RequestParam String status) {
//        service.verifyPerfectTransaction(transactionId, status);
//    }

//    @PostMapping("/updateAccountScore/{accountId}")
//    public void updateAccountScore(@PathVariable Long accountId) {
//        // Call the service method to update the account score
//        service.updateAccountScore(accountId);
//    }

}