package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.entities.Transaction;
import tn.esprit.easyfund.repositories.BankAccountRep;
import tn.esprit.easyfund.repositories.TransactionRep;


import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRep transactionRepository;
    @Autowired
    private BankAccountRep Rep;



    public Transaction addTransaction(Transaction transaction, Long bankAccountId) {
        BankAccount bankAccount = Rep.findById(bankAccountId).orElse(null);
        if (bankAccount != null) {
            transaction.setBankAccount(bankAccount);
            return transactionRepository.save(transaction);
        } else {
            // Handle scenario where bank account with provided ID is not found
            return null;
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    // Example method to get all transactions for a specific bank account

    public Transaction updateTransaction(Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(updatedTransaction.getTrans_id());
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            // Update transaction properties
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setAcc_from(updatedTransaction.getAcc_from());
            transaction.setAcc_to(updatedTransaction.getAcc_to());
            // Update other properties as needed
            // Save the updated transaction
            return transactionRepository.save(transaction);
        } else {
            // Handle scenario where transaction with provided ID is not found
            return null;
        }
    }
    public Transaction performTransaction(String senderAccountId, String receiverAccountId, float amount) {
        Long senderId = Long.parseLong(senderAccountId);
        Long receiverId = Long.parseLong(receiverAccountId);
        BankAccount senderAccount = Rep.findById(senderId).orElse(null);
        BankAccount receiverAccount = Rep.findById(receiverId).orElse(null);

        if (senderAccount != null && receiverAccount != null && senderAccount.getBalance() >= amount) {
            // Deduct amount from sender's account
            senderAccount.setBalance(senderAccount.getBalance() - amount);
            Rep.save(senderAccount);

            // Add amount to receiver's account
            receiverAccount.setBalance(receiverAccount.getBalance() + amount);
            Rep.save(receiverAccount);

            // Create transaction record
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setAcc_from(senderAccount.getAccount_id().toString());
            transaction.setAcc_to(receiverAccount.getAccount_id().toString());
            // Set other transaction details like date, etc.

            return transactionRepository.save(transaction);
        } else {
            // Handle scenario where either sender or receiver account is not found, or sender does not have enough balance
            return null;
        }
    }

}
