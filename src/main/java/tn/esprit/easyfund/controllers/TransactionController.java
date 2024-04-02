package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Transaction;
import tn.esprit.easyfund.entities.TransactionType;
import tn.esprit.easyfund.services.TransactionAnalysisService;
import tn.esprit.easyfund.services.TransactionAnalysisServiceImpl;
import tn.esprit.easyfund.services.TransactionService;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionAnalysisServiceImpl transactionAnalysisService;

    @PostMapping("/addTransaction/{senderAccountId}/{receiverAccountId}")
    public Transaction addTransaction(@RequestBody Transaction transaction,
                                      @PathVariable Long senderAccountId,
                                      @PathVariable Long receiverAccountId) {
        return transactionService.addTransaction(transaction, senderAccountId, receiverAccountId);
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

    @PostMapping("/perfectTransaction/{senderAccountId}/{receiverAccountId}")
    public Transaction perfectTransaction(
            @PathVariable String senderAccountId,
            @PathVariable String receiverAccountId,
            @RequestParam float amount) {
        return transactionService.perfectTransaction(senderAccountId, receiverAccountId, amount);
    }

    @PostMapping("/withdrawTransaction/{senderAccountId}")
    public Transaction withdrawTransaction(@PathVariable String senderAccountId, @RequestParam float amount) {
        return transactionService.withdrawTransaction(senderAccountId, amount);
    }
    @PostMapping("/depositTransaction/{receiverAccountId}")
    public Transaction depositTransaction(@PathVariable String receiverAccountId, @RequestParam float amount) {
        return transactionService.depositTransaction(receiverAccountId, amount);
    }

    @GetMapping("/accountActivity/{accountId}")
    public ResponseEntity<Map<String, Object>> getTransactionTypeDistribution(@PathVariable Long accountId) {
        // Retrieve transaction type distribution from the service
        Map<String, Object> response = transactionAnalysisService.getTransactionTypeDistribution(accountId);

        // Check if the response is empty (no transactions found for the account)
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Return ResponseEntity with the transaction type distribution
        return ResponseEntity.ok(response);
    }

    @GetMapping("/explore-relationships")
    public ResponseEntity<Map<String, Object>> exploreTransactionRelationships() {
        Map<String, Object> relationships = transactionAnalysisService.exploreTransactionRelationships();
        return ResponseEntity.ok(relationships);
    }


}