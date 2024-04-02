package tn.esprit.easyfund.services;

import java.util.List;

import tn.esprit.easyfund.entities.InsuranceContract;


public interface IInsuranceContractService {
    InsuranceContract getInsuranceContractById(Long insuranceContractId);
    List<InsuranceContract> getAllInsurancesContracts();
    InsuranceContract saveInsuranceContract(InsuranceContract insuranceContract,Long insuranceId);
    InsuranceContract updateInsuranceContract(Long insuranceContractId, InsuranceContract updatedInsuranceContract);
    List<InsuranceContract> getInsuranceContractsAssignedToAgent();



    void deleteInsuranceContract(Long insuranceContractId);

}
