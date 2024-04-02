package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.InsuranceContract;

import java.util.List;

public interface IInsuranceContractService {
    InsuranceContract getInsuranceContractById(Long insuranceContractId);
    List<InsuranceContract> getAllInsurancesContracts();
    InsuranceContract saveInsuranceContract(InsuranceContract insuranceContract,Long insuranceId);
    InsuranceContract updateInsuranceContract(Long insuranceContractId, InsuranceContract updatedInsuranceContract);

    void deleteInsuranceContract(Long insuranceContractId);

}
