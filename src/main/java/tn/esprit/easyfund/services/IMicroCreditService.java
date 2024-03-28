package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;

import java.util.List;

public interface IMicroCreditService {

    MicroCredit createMicroCredit(MicroCredit microCredit);

    MicroCredit findCreditById(Long id);

    List<MicroCredit> findAllCredits();

    void deleteCredit(Long id);

    MicroCredit updateCredit(MicroCredit microCredit);

    MicroCredit updateStatus(Long id, CreditStatus status);


    List<MicroCredit> getCreditsByStatus(CreditStatus status);

    List<MicroCredit> getCreditsByType(CreditType type);

    List<MicroCredit> getCreditByAccountId(Long id);
}
