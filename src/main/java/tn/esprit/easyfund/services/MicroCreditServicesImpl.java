package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.repositories.IMicroCreditRepositories;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class MicroCreditServicesImpl implements IMicroCreditService {

    @Autowired
    private IMicroCreditRepositories microCreditRepositories;

    @Override
    public MicroCredit createMicroCredit(MicroCredit microCredit) {
        return microCreditRepositories.save(microCredit);

    }

    @Override
    public MicroCredit findCreditById(Long id) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            System.out.println("Credit found");
        } else {
            System.out.println("Credit not found");
        }
        return credit;
    }

    @Override
    public List<MicroCredit> findAllCredits() {
        List<MicroCredit> credits = microCreditRepositories.findAll();
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;

    }

    @Override
    public void deleteCredit(Long id) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            assert credit != null;
            microCreditRepositories.delete(credit);
            System.out.println("Credit deleted");
        } else {
            System.out.println("Credit not found");
        }
    }

    @Override
    public MicroCredit updateCredit(MicroCredit microCredit) {
        Long creditId = microCredit.getMicroCreditId();
        if (creditId != null && microCreditRepositories.existsById(creditId)) {
            System.out.println("Credit updated");
            return microCreditRepositories.save(microCredit);
        } else {
            System.out.println("Credit not found");
            return null;
        }
    }


    @Override
    public MicroCredit updateStatus(Long id, CreditStatus status) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            assert credit != null;
            credit.setCreditStatus(status);
            System.out.println("Credit Status Changed...");
            return microCreditRepositories.save(credit);
        } else {
            System.out.println("Credit not found");
        }
        return null;
    }

    @Override
    public List<MicroCredit> getCreditsByStatus(CreditStatus status) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByStatus(status);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }

    @Override
    public List<MicroCredit> getCreditsByType(CreditType type) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByType(type);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }

    @Override
    public List<MicroCredit> getCreditByAccountId(Long id) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByAccountID(id);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }
}
