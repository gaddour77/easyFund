package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.BankAccount;
import tn.esprit.easyfund.entities.Transaction;
import tn.esprit.easyfund.entities.Status;
import tn.esprit.easyfund.entities.TransactionType;
import tn.esprit.easyfund.repositories.BankAccountRep;
import tn.esprit.easyfund.repositories.TransactionRep;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private TransactionRep transactionRepository;
    @Autowired
    private BankAccountRep Rep;
    @Autowired
    private NotificationService notificationService;


    private String generateRandomCountRef() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10); // Generating a random alphanumeric string of length 10
    }
    public Transaction addTransaction(Transaction transaction, Long senderAccountId, Long receiverAccountId) {
        BankAccount senderAccount = Rep.findById(senderAccountId).orElse(null);
        BankAccount receiverAccount = Rep.findById(receiverAccountId).orElse(null);

        if (senderAccount != null && receiverAccount != null) {
            transaction.setAcc_from(senderAccount.getAccount_id().toString());
            transaction.setAcc_to(receiverAccount.getAccount_id().toString());
            transaction.setRef_number(generateRandomCountRef());
            transaction.setBankAccount(senderAccount);

            // Assuming you set other properties of the transaction as needed

            return transactionRepository.save(transaction);
        } else {
            // Handle scenario where either sender or receiver account is not found
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
            transaction.setTrans_fee(updatedTransaction.getTrans_fee());
            transaction.setRef_number(updatedTransaction.getRef_number());
            // Update other properties as needed
            // Save the updated transaction
            return transactionRepository.save(transaction);
        } else {
            // Handle scenario where transaction with provided ID is not found
            return null;
        }
    }
    public Transaction perfectTransaction(String senderAccountId, String receiverAccountId, float amount) {
        Long senderId = Long.parseLong(senderAccountId);
        Long receiverId = Long.parseLong(receiverAccountId);
        BankAccount senderAccount = Rep.findById(senderId).orElse(null);
        BankAccount receiverAccount = Rep.findById(receiverId).orElse(null);
        String refNumber = generateRandomRefNumber();

        if (senderAccount != null && receiverAccount != null && senderAccount.is_active()
                && receiverAccount.is_active() && senderAccount.getBalance() >= amount) {
            // Calculate transaction fee
            float transactionFee;
            if (amount < 60000) {
                // Calculate transaction fee (2.5% of the transaction amount)
                transactionFee = 0.025f * amount;
            } else {
                // For amounts greater than or equal to 60000, set transaction fee to 0
                transactionFee = 1500;
            }// 5% of the transaction amount

            // Create transaction record with pending status
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setAcc_from(senderAccount.getAccount_id().toString());
            transaction.setAcc_to(receiverAccount.getAccount_id().toString());
            transaction.setTrans_fee(transactionFee);
            transaction.setRef_number(refNumber);
            transaction.setTrans_date(LocalDate.now());
            transaction.setTransType(TransactionType.transfer);
            transaction.setBankAccount(senderAccount);
            transaction.setStat(Status.pending);
            transaction.setCurrency("TND");// Set initial status as pending

            // Save the transaction
            // Save the transaction
            transaction = transactionRepository.save(transaction);

            // Notify sender about the pending transaction
            String senderPhoneNumber = "+21655502764"; // Assuming this is the sender's phone number
            String receiverPhoneNumber = "+21655502764";
            String senderMessage = String.format("You have initiated a transfer of %.2f TND to account %s. This transaction is pending.", amount, receiverAccountId);
            notificationService.sendSMS(senderPhoneNumber, senderMessage);

            // Notify receiver about the pending transaction
            String receiverMessage = String.format("You have received a transfer of %.2f TND from account %s. This transaction is pending.", amount, senderAccountId);
            notificationService.sendSMS(receiverPhoneNumber, receiverMessage);

            return transaction;
        } else {
            // Handle scenario where either sender or receiver account is not found,
            // or sender does not have enough balance
            return null;
        }
    }

    private String generateRandomRefNumber() {
        // Generate a random UUID and remove hyphens
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // Trim to the desired length (e.g., 10 characters)
        return uuid.substring(0, 10);
    }

    public Transaction withdrawTransaction(String senderAccountId, float amount) {
        Long senderId = Long.parseLong(senderAccountId);
        BankAccount senderAccount = Rep.findById(senderId).orElse(null);
        String refNumber = generateRandomRefNumber();

        if (senderAccount != null && senderAccount.getBalance() >= amount && senderAccount.is_active()) {
            // Create transaction record with status set to "pending"
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setAcc_from(senderAccountId);
            transaction.setAcc_to("N/A");
            transaction.setCurrency("TND");
            transaction.setRef_number(refNumber);
            transaction.setTrans_fee(0); // Assuming no transaction fee for withdrawal
            transaction.setTrans_date(LocalDate.now());
            transaction.setTransType(TransactionType.withdraw);
            transaction.setBankAccount(senderAccount);
            transaction.setStat(Status.pending); // Set initial status to "pending"

            // Save the transaction record
            // Save the transaction record
            transaction = transactionRepository.save(transaction);

            // Send notification to sender
            String senderPhoneNumber = "+21655502764";
            String message = String.format("You have initiated a withdrawal of %.2f TND from your account. This transaction is pending until verification.", amount);
            notificationService.sendSMS(senderPhoneNumber, message);

            // Return the transaction
            return transaction;
        } else {
            // Handle scenario where sender account is not found or does not have enough balance
            return null;
        }
    }


    public Transaction depositTransaction(String receiverAccountId, float amount) {
        Long receiverId = Long.parseLong(receiverAccountId);
        BankAccount receiverAccount = Rep.findById(receiverId).orElse(null);
        String refNumber = generateRandomRefNumber();

        if (receiverAccount != null && receiverAccount.is_active()) {
            // Create transaction record
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setAcc_from("N/A");
            transaction.setAcc_to(receiverAccountId);
            transaction.setCurrency("TND");
            transaction.setRef_number(refNumber);
            transaction.setTrans_fee(0); // Assuming no transaction fee for deposit
            transaction.setTrans_date(LocalDate.now());
            transaction.setTransType(TransactionType.deposit);
            transaction.setStat(Status.pending); // Set transaction status to pending initially
            transaction.setBankAccount(receiverAccount);

            transaction = transactionRepository.save(transaction);

            // Send notification to receiver
            String senderPhoneNumber = "+21655502764";
            String message = String.format("You have received a deposit of %.2f TND to your account. This transaction is pending until verification.", amount);
            notificationService.sendSMS(senderPhoneNumber, message);

            return transaction;
        } else {
            // Handle scenario where receiver account is not found
            return null;
        }
    }




}
