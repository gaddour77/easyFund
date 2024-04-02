package tn.esprit.easyfund.services;

public class BannedUserException extends RuntimeException {
    public BannedUserException(String message) {
        super(message);
    }
}
