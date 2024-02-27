package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Transaction;
import tn.esprit.easyfund.services.TransactionService;


import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/addTransaction/{bankAccountId}")
    public Transaction addTransaction(@RequestBody Transaction transaction, @PathVariable Long bankAccountId) {
        return transactionService.addTransaction(transaction, bankAccountId);
    }

    @GetMapping("/getTransactions")
    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/getTransaction/{id}")
    public Transaction getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/updateTransaction")
    public Transaction updateTransaction(@RequestBody Transaction transaction) {
        return transactionService.updateTransaction(transaction);
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

    @PostMapping("/performTransaction")
    public Transaction performTransaction(@RequestBody Transaction request) {
        // Assuming TransactionRequest is a DTO that contains senderAccountId, receiverAccountId, and amount fields
        String senderAccountId = request.getAcc_from();
        String receiverAccountId = request.getAcc_to();
        float amount = request.getAmount();
        return transactionService.performTransaction(senderAccountId, receiverAccountId, amount);
    }

}

