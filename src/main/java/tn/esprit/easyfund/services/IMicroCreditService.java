package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;
import java.util.List;

public interface IMicroCreditService {

    MicroCredit createMicroCredit(MicroCredit microCredit);
    List<MicroCredit> findAllCredits();
    MicroCredit findCreditById(Long id);
    void deleteCredit(Long id);

    MicroCredit updateCredit(MicroCredit microCredit);

    MicroCredit updateStatus(Long id, CreditStatus status);



    void archiveCredit(Long id);

    List<MicroCredit> getCreditsByStatus(CreditStatus status);

    List<MicroCredit> getCreditsByType(CreditType type);
}
