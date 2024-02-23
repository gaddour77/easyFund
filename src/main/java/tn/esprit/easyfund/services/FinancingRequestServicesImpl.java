package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class FinancingRequestServicesImpl implements IFinancingRequestServices{
    private IFinancingRequestRepository financingRequestRepository;
    public FinancingRequest addFinancing(FinancingRequest financingRequest){
        return financingRequestRepository.save(financingRequest);
    }
    public List<FinancingRequest> addFinancings(List<FinancingRequest> requests){
        return financingRequestRepository.saveAll(requests);
    }
    public FinancingRequest findById(long id){
        return financingRequestRepository.findById(id).orElse(null);
    }
    public List<FinancingRequest> findAll(){
        return financingRequestRepository.findAll();
    }
    public List<FinancingRequest>  findByOffer (long id){
        return financingRequestRepository.findByOffer(id);
    }
    public FinancingRequest update(FinancingRequest financingRequest){
        FinancingRequest currentRequest = financingRequestRepository.findById(financingRequest.getFinancingRequestId()).orElse(null);
        currentRequest.setDateFinancingRequest(financingRequest.getDateFinancingRequest());
        currentRequest.setRequestStatus(financingRequest.getRequestStatus());
        currentRequest.setFinalDate(financingRequest.getFinalDate());
        return financingRequestRepository.save(currentRequest);

    }
    public String delete(long id){
        financingRequestRepository.deleteById(id);
        return "financing request deleted :"+id;
    }
}
