package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Insurance;
import tn.esprit.easyfund.repositories.InsuranceRepository;

import java.util.List;

@Service
public class InsuranceServiceImp implements IInsuranceService{
    @Autowired
    private InsuranceRepository insuranceRepository;

    @Override
    public Insurance getInsuranceById(Long insuranceId) {
        return insuranceRepository.findById(insuranceId).orElse(null);
    }

   

    @Override
    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }
    
    @Override
    public Insurance updateInsurance(Long insuranceId, Insurance updatedInsurance) {
        Insurance existingInsurance = insuranceRepository.findById(insuranceId).orElse(null);

        if (existingInsurance != null) {

            existingInsurance.setType(updatedInsurance.getType());
            existingInsurance.setBeneficiary(updatedInsurance.getBeneficiary());
            existingInsurance.setCoverageAmount(updatedInsurance.getCoverageAmount());
            existingInsurance.setName(updatedInsurance.getName());


            return insuranceRepository.save(existingInsurance);
        } else {
            return null;
        }
    }
    @Override
    public void deleteInsurance(Long insuranceId) {
        insuranceRepository.deleteById(insuranceId);
    }



    @Override
    public List<Insurance> getAllInsurances() {
        return  insuranceRepository.findAll();
    }


}
