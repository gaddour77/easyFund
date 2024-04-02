package tn.esprit.easyfund.services;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.InvestementRequest;
import tn.esprit.easyfund.repositories.InvestementRequestRepository;
import tn.esprit.easyfund.services.ICRUDService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InvestementRequestService implements ICRUDService<InvestementRequest,Integer> {
    @Autowired
    InvestementRequestRepository investementRequestRepository ;
    @Override
    public List<InvestementRequest> findAll() {
        return investementRequestRepository.findAll();
    }

    @Override
    public InvestementRequest retrieveItem(Integer idItem) {
        return investementRequestRepository.findById(idItem).get();
    }

    @Override
    public InvestementRequest add(InvestementRequest class1) {
        return investementRequestRepository.save(class1);
    }

    @Override
    public void delete(Integer id) {
        investementRequestRepository.deleteById(id);

    }

    @Override
    public InvestementRequest update(Integer id ,InvestementRequest InvestementRequest) {
        Optional<InvestementRequest> existingInvestementRequestOptional = investementRequestRepository.findById(id);

        if (existingInvestementRequestOptional.isPresent()) {
            InvestementRequest existingInvestementRequest = existingInvestementRequestOptional.get();
            existingInvestementRequest.setAmountInvested(InvestementRequest.getAmountInvested());
            if(existingInvestementRequestOptional.get().getStatusInv()!=InvestementRequest.getStatusInv()){
                existingInvestementRequest.setStatusInv(InvestementRequest.getStatusInv());
            }
            return investementRequestRepository.save(existingInvestementRequest);
        } else {
            return null;
        }
    }
}
