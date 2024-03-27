package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.exceptions.MicroCreditNotFoundException;
import tn.esprit.easyfund.repositories.IMicroCreditRepositories;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class IMicroCreditServicesImpl implements IMicroCreditService {

    @Autowired
    private IMicroCreditRepositories microCreditRepositories;

    @Override
    public MicroCredit createMicroCredit(MicroCredit microCredit) {
        return microCreditRepositories.save(microCredit);
    }

    @Override
    public MicroCredit findCreditById(Long id) {
        return microCreditRepositories.findById(id).get();
    }

    @Override
    public List<MicroCredit> findAllCredits() {
        return microCreditRepositories.findAll();
    }

    @Override
    public void deleteCredit(Long id) {
        MicroCredit credit= microCreditRepositories.findById(id).get();
        if (microCreditRepositories.findById(id).isPresent()) {
            microCreditRepositories.delete(credit);
            System.out.println("Credit deleted");
        } else {
            System.out.println("Credit not found");
        }
    }

    @Override
    public MicroCredit updateCredit(MicroCredit credit) {
        return null;
    }

    @Override
    public MicroCredit updateStatus(Long id, CreditStatus status) {
        return microCreditRepositories.findById(id)
                .map(credit -> {
                    credit.setCreditStatus(status);
                    return microCreditRepositories.save(credit);
                })
                .orElseThrow(() -> new MicroCreditNotFoundException(id));
    }

    @Override
    public List<MicroCredit> getCreditsByStatus(CreditStatus status) {
        return microCreditRepositories.retrieveCreditsByStatus(status);

    }

    @Override
    public List<MicroCredit> getCreditsByType(CreditType type) {
        return microCreditRepositories.retrieveCreditsByType(type);
    }



    @Override
    public void archiveCredit(Long id) {
    }


}
