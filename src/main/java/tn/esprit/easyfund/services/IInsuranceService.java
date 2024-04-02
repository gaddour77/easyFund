package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Insurance;

import java.util.List;

public interface IInsuranceService {
    Insurance getInsuranceById(Long insuranceId);
    List<Insurance> getAllInsurances();
    Insurance saveInsurance(Insurance insurance);
    Insurance updateInsurance(Long insuranceId, Insurance updatedInsurance);
    void deleteInsurance(Long insuranceId);
}
