package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.FinancingRequest;

import java.util.List;

public interface IFinancingRequestServices {
    public FinancingRequest addFinancing(FinancingRequest financingRequest);
    public List<FinancingRequest> addFinancings(List<FinancingRequest> requests);
    public List<FinancingRequest> findAll();
    public FinancingRequest findById(long id);
    public List<FinancingRequest>  findByOffer (long id);
    public FinancingRequest update(FinancingRequest financingRequest);
    public String delete(long id);
    public List<FinancingRequest> findByUser(long id);
}
