package tn.esprit.easyfund.controllers;

public class MicroCreditAlreadyExistsException extends RuntimeException {

    public MicroCreditAlreadyExistsException(Long id) {
        super("MicroCredit with ID " + id + " not found");
    }

}
