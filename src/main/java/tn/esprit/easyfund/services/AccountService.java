package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.entities.Account;
import tn.esprit.easyfund.repositories.IAccountRepository;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private IAccountRepository repository;
    @Autowired
    private IUserRepository userRepository;

    public Account saveAccount(Long userId, Account Account) {
        User user = userRepository.findById(userId).orElse(null);
        System.out.println("User with ID " + userId + "  found.");
        if (user != null) {
            Account.setUser(user);
            Account savedAccount = repository.save(Account);
            // Update the User entity with the new Account
            user.setAccount(savedAccount);
            userRepository.save(user);
            return savedAccount;
        } else {System.out.println("User with ID " + userId + " not found.");
            // Or throw an exception to indicate the error
            throw new RuntimeException("User with ID " + userId + " not found.");
            // Handle the case where the user with the given ID is not found

        }
    }
    public List<Account> getAccounts(){
        return repository.findAll();
    }

    public Account getAccountId(Long id){
        return repository.findById(id).orElse(null);

    }
    public String deleteAccount(Long id) {
        Optional<Account> optionalAccount = repository.findById(id);
        if (optionalAccount.isPresent()) {
            Account Account = optionalAccount.get();
            User user = Account.getUser();
            if (user != null) {
                user.setAccount(null); // Remove association with User
            }
            repository.deleteById(id);
            return "Bank account removed: " + id;
        } else {
            return "Bank account with ID " + id + " not found.";
        }
    }

    public Account updateAccount(Account Account){
        Account existingAccount=repository.findById(Account.getAccountId()).orElse(null);
        assert existingAccount != null;
        existingAccount.setBalance(Account.getBalance());
        existingAccount.setScore(Account.getScore());
        existingAccount.setUpdateDate(Account.getUpdateDate());
        existingAccount.setCreationDate(Account.getCreationDate());
        existingAccount.setAccountType(Account.getAccountType());
        return repository.save(existingAccount);
    }

    public String deactivateAccount(Long accountId) {
        Account account = repository.findById(accountId).orElse(null);
        if (account != null) {
            account.setActive(false);
            repository.save(account);
            return "Account with ID " + accountId + " has been deactivated.";
        } else {
            return "Account with ID " + accountId + " not found.";
        }
    }
    public String activateAccount(Long id) {
        Optional<Account> optionalAccount = repository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (!account.isActive()) {
                account.setActive(true);
                repository.save(account);
                return "Account with ID " + id + " has been activated.";

            } else {
                return "Account with ID " + id + " is already active.";
            }
        } else {
            return "Account with ID " + id + " not found.";
        }
    }
}