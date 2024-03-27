package tn.esprit.easyfund.exceptions;

public class MicroCreditNotFoundException extends RuntimeException {

    public MicroCreditNotFoundException(Long id) {
        super("MicroCredit with ID " + id + " not found");
    }

}
