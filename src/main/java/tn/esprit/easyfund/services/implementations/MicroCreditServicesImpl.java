package tn.esprit.easyfund.services.implementations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.repositories.IMicroCreditRepositories;
import tn.esprit.easyfund.services.interfaces.IMicroCreditServices;

import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor


@Service
public class MicroCreditServicesImpl implements IMicroCreditServices {
    @Autowired
    private IMicroCreditRepositories microCreditRepositories;

   @Override
   public MicroCredit addCredit(MicroCredit u) {
      return microCreditRepositories.save(u);
   }

   @Override
    public List<MicroCredit> findAllCredits() {
    return microCreditRepositories.findAll();
    }

    @Override
    public MicroCredit findCreditById(long id) {
        return null;
    }

    @Override
    public void archiveCredit(long id) {

    }

    @Override
    public MicroCredit updateCredit(MicroCredit c)
    {
        return null;
    }

    @Override
    public MicroCredit updateStatus(long id) {
        return null;
    }

}
