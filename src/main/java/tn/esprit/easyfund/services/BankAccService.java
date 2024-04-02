package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.*;
import tn.esprit.easyfund.repositories.BankAccountRep;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.repositories.TransactionRep;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankAccService {
    @Autowired
    private BankAccountRep repository;
    @Autowired
    private TransactionRep transactionRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private IUserRepository userRepository;

            public BankAccount saveAccount(Long userId, BankAccount bankaccount) {
                User user = userRepository.findById(userId).orElse(null);
                System.out.println("User with ID " + userId + "  found.");
                if (user != null) {
                    bankaccount.setUser(user);
                    bankaccount.setCount_ref(generateRandomCountRef());
                    BankAccount savedAccount = repository.save(bankaccount);
                    // Update the User entity with the new BankAccount
                    user.setAccount(savedAccount);
                    userRepository.save(user);
                    return savedAccount;
                } else {System.out.println("User with ID " + userId + " not found.");
                    // Or throw an exception to indicate the error
                    throw new RuntimeException("User with ID " + userId + " not found.");
                    // Handle the case where the user with the given ID is not found

                }
            }
    private String generateRandomCountRef() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10); // Generating a random alphanumeric string of length 10
    }
    public List<BankAccount> getAccounts(){
        List<BankAccount> bankAccounts = repository.findAll();
        return bankAccounts;
    }

    public BankAccount getAccountId(Long id){
        return repository.findById(id).orElse(null);

    }
    public String deleteAccount(Long id) {
        Optional<BankAccount> optionalBankAccount = repository.findById(id);
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            User user = bankAccount.getUser();
            if (user != null) {
                user.setAccount(null); // Remove association with User
            }
            repository.deleteById(id);
            return "Bank account removed: " + id;
        } else {
            return "Bank account with ID " + id + " not found.";
        }
    }

    public BankAccount updateAccount(BankAccount bankAccount){
        BankAccount existingAccount=repository.findById(bankAccount.getAccount_id()).orElse(null);
        existingAccount.setBalance(bankAccount.getBalance());
        existingAccount.setScore(bankAccount.getScore());
        existingAccount.set_active(bankAccount.is_active());
        existingAccount.setUpdate_date(bankAccount.getUpdate_date());
        existingAccount.setCreate_date(bankAccount.getCreate_date());
        existingAccount.setTypeAcc(bankAccount.getTypeAcc());
        return repository.save(existingAccount);
    }

    public String deactivateAccount(Long accountId) {
        BankAccount account = repository.findById(accountId).orElse(null);
        if (account != null) {
            account.set_active(false);
            repository.save(account);
            String senderPhoneNumber = "+21655502764";
            String message1 = String.format("Your Account  has been deactivated.");
            notificationService.sendSMS(senderPhoneNumber, message1);
            return "Account with ID " + accountId + " has been deactivated.";
        } else {
            return "Account with ID " + accountId + " not found.";
        }
    }
    public String activateAccount(Long id) {
        Optional<BankAccount> optionalAccount = repository.findById(id);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            if (!account.is_active()) {
                account.set_active(true);
                repository.save(account);
                String senderPhoneNumber = "+21655502764";
                String message1 = String.format("Your Account  has been activated.");
                notificationService.sendSMS(senderPhoneNumber, message1);
                return "Account with ID " + id + " has been activated.";

            } else {
                return "Account with ID " + id + " is already active.";
            }
        } else {
            return "Account with ID " + id + " not found.";
        }
    }

    public void verifyDepositTransaction(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction != null && transaction.getStat() == Status.pending) {
            BankAccount receiverAccount = transaction.getBankAccount();
            if (receiverAccount != null) {
                if (status.equalsIgnoreCase("accepted")) {
                    // Add the amount to the receiver's balance
                    float amount = transaction.getAmount();
                    receiverAccount.setBalance(receiverAccount.getBalance() + amount);
                    updateAccountScore(receiverAccount.getAccount_id());
                    // Set the update_date to the current date
                    receiverAccount.setUpdate_date(LocalDate.now());
                    repository.save(receiverAccount);

                    // Update the transaction status to accepted
                    transaction.setStat(Status.accepted);
                    transactionRepository.save(transaction);
                    String senderPhoneNumber = "+21655502764";
                    String message1 = String.format("Your deposit of %.2f TND has been accepted. Your new balance is %.2f TND.", amount, receiverAccount.getBalance());
                    notificationService.sendSMS(senderPhoneNumber, message1);
                } else if (status.equalsIgnoreCase("failed")) {
                    // Update the transaction status to denied
                    transaction.setStat(Status.failed);
                    transactionRepository.save(transaction);
                    String senderPhoneNumber = "+21655502764";
                    String message2 = String.format("Your deposit of %.2f TND has been refused. Please contact customer support for more information.", transaction.getAmount());
                    notificationService.sendSMS(senderPhoneNumber, message2);
                }
            }
        }
    }
    public void verifyWithdrawTransaction(Long transactionId, String status) {
        // Find the transaction by ID
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction != null && transaction.getStat() == Status.pending) {
            // Get the sender's account
            BankAccount senderAccount = transaction.getBankAccount();

            if (senderAccount != null) {
                if (status.equalsIgnoreCase("accepted")) {
                    // Deduct the amount from the sender's balance
                    float amount = transaction.getAmount();
                    senderAccount.setBalance(senderAccount.getBalance() - amount);
                    // Set the update_date to the current date
                    senderAccount.setUpdate_date(LocalDate.now());
                    repository.save(senderAccount);

                    // Update the transaction status to accepted
                    transaction.setStat(Status.accepted);
                    transactionRepository.save(transaction);
                    String senderPhoneNumber = "+21655502764";
                    String message1 = String.format("Your withdrawal of %.2f TND has been accepted. Your new balance is %.2f TND.", amount, senderAccount.getBalance());
                    notificationService.sendSMS(senderPhoneNumber, message1);
                } else if (status.equalsIgnoreCase("failed")) {
                    // Update the transaction status to denied
                    transaction.setStat(Status.failed);
                    transactionRepository.save(transaction);
                    String senderPhoneNumber = "+21655502764";
                    String message2 = String.format("Your withdrawal of %.2f TND has been refused. Please contact customer support for more information.", transaction.getAmount());
                    notificationService.sendSMS(senderPhoneNumber, message2);

                } else {
                    // Handle invalid status
                    // You may throw an exception or handle it according to your requirements
                }
            }
        }
    }

    public void verifyPerfectTransaction(Long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction != null && transaction.getStat() == Status.pending) {
            if (status.equalsIgnoreCase("accepted")) {
                // Update the transaction status to accepted
                transaction.setStat(Status.accepted);
                // Update the bank account balances
                BankAccount senderAccount = transaction.getBankAccount();
                BankAccount receiverAccount = repository.findById(Long.parseLong(transaction.getAcc_to())).orElse(null);
                BankAccount feeAccount = repository.findByCount_ref("THEBANK");
                if (senderAccount != null && receiverAccount != null) {
                    float amount = transaction.getAmount();
                    float transactionFee = transaction.getTrans_fee();
                    // Deduct amount plus transaction fee from sender's account
                    senderAccount.setBalance(senderAccount.getBalance() - (amount + transactionFee));
                    // Add amount to receiver's account

                    receiverAccount.setBalance(receiverAccount.getBalance() + amount);
                    feeAccount.setBalance(feeAccount.getBalance() + transactionFee);
                    // Set the update_date to the current date
                    receiverAccount.setUpdate_date(LocalDate.now());
                    senderAccount.setUpdate_date(LocalDate.now());
                    repository.saveAll(List.of(senderAccount, receiverAccount, feeAccount));
                    // Update scores for sender and receiver accounts
                    updateAccountScore(senderAccount.getAccount_id());
                    updateAccountScore(Long.parseLong(transaction.getAcc_to()));
                    // Set the update_date to the current date
                    receiverAccount.setUpdate_date(LocalDate.now());
                    senderAccount.setUpdate_date(LocalDate.now());
                    String senderPhoneNumber = "+21655502764"; // Assuming this is the sender's phone number
                    String receiverPhoneNumber = "+21655502764";
                    String senderMessage = String.format("Your transfer of %.2f TND to account %s is successful", amount, receiverAccount.getAccount_id());
                    notificationService.sendSMS(senderPhoneNumber, senderMessage);

                    // Notify receiver about the pending transaction
                    String receiverMessage = String.format("You have received a transfer of %.2f TND from account %s.", amount, senderAccount.getAccount_id());
                    notificationService.sendSMS(receiverPhoneNumber, receiverMessage);

                }
            } else if (status.equalsIgnoreCase("denied")) {
                // Update the transaction status to denied
                transaction.setStat(Status.failed);

                float amount = transaction.getAmount();
                String senderPhoneNumber = "+21655502764";
                String senderMessage = String.format("Your transfer of %.2f TND  has been denied", amount);
                notificationService.sendSMS(senderPhoneNumber, senderMessage);}
            transactionRepository.save(transaction);
        }
    }

    public void updateAccountScore(Long accountId) {
        BankAccount account = repository.findById(accountId).orElse(null);
        if (account != null) {
            int score = calculateAccountScore(account); // Calculate score based on certain criteria
            account.setScore(score);
            repository.save(account);
        }
    }

    // Example method to calculate the score of a bank account
    private int calculateAccountScore(BankAccount account) {
        int score = 0;
        List<Transaction> transactions = transactionRepository.findByBankAccount(account);
        for (Transaction transaction : transactions) {
            // Example: Increase score for each transaction
            score += calculateTransactionScore(transaction);
        }
        // You can include other criteria and calculations for the score here
        return score;
    }

    // Example method to calculate the score contribution of a transaction
    private int calculateTransactionScore(Transaction transaction) {
        int score = 0;

        // Example: Increase score based on transaction amount
        float amount = transaction.getAmount();
        if (amount > 1000) {
            score += 5; // Increase score by 5 if transaction amount is greater than 1000
        } else if (amount > 500) {
            score += 3; // Increase score by 3 if transaction amount is greater than 500
        } else {
            score += 1; // Increase score by 1 for other transaction amounts
        }

        // Example: Increase score for specific transaction types
        TransactionType transactionType = transaction.getTransType();
        if (transactionType == TransactionType.transfer) {
            score += 2; // Increase score by 2 for transfer transactions
        } else if (transactionType == TransactionType.deposit) {
            score += 1; // Increase score by 1 for deposit transactions
        }

        // You can add more criteria and adjust the scoring mechanism as needed

        return score;
    }

}