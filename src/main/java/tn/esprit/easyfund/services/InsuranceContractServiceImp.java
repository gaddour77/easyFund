package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Insurance;
import tn.esprit.easyfund.entities.InsuranceContract;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.repositories.InsuranceContractRepository;

import java.util.List;


@Service
public class InsuranceContractServiceImp implements IInsuranceContractService{
    @Autowired
    private InsuranceContractRepository insuranceContractRepository;

    @Autowired 
    private IInsuranceService  insuranceService;

    @Autowired
    private IUserRepository userRepository;


    @Override
    public InsuranceContract getInsuranceContractById(Long insuranceContractId) {
        return insuranceContractRepository.findById(insuranceContractId).orElse(null);
    }


    @Override
    public InsuranceContract saveInsuranceContract(InsuranceContract insuranceContract,Long insuranceId) {
        // Add any additional business logic if needed

        // // Get the current user details from the security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // // // Find the user based on the username (assuming username is the email)
        Insurance insurance = insuranceService.getInsuranceById(insuranceId);

             // Find the user based on the username (assuming username is the email)
             User user = userRepository.findByEmail(userDetails.getUsername())
             .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

     // Set the user for the claim
     insuranceContract.setUser(user);
     insuranceContract.setUserId(user.getUserId());



        // if insurance  does not exist then throw an exception
        if (insurance == null){
            throw new IllegalArgumentException("No such");
        }

        // Set the user for the insuranceContract
        insuranceContract.setInsurance(insurance);
        insuranceContract.setInsuranceId(insurance.getId());
        // insuranceContract.setInsuranceContractStatus(InsuranceContractStatus.PENDING);

        // Assign the insuranceContract to an agent
        return insuranceContractRepository.save(insuranceContract);


    }
    @Override
    public InsuranceContract updateInsuranceContract(Long insuranceContractId, InsuranceContract updatedInsuranceContract) {
        InsuranceContract existingInsuranceContract = insuranceContractRepository.findById(insuranceContractId).orElse(null);

        if (existingInsuranceContract != null) {

            existingInsuranceContract.setStatus(updatedInsuranceContract.getStatus());
            existingInsuranceContract.setPaidAmount(updatedInsuranceContract.getPaidAmount());
            existingInsuranceContract.setStatus(updatedInsuranceContract.getStatus());


            return insuranceContractRepository.save(existingInsuranceContract);
        } else {
            // Handle insuranceContract not found
            return null;
        }
    }
    @Override
    public void deleteInsuranceContract(Long insuranceContractId) {
        insuranceContractRepository.deleteById(insuranceContractId);
    }


    @Override
    public List<InsuranceContract> getAllInsurancesContracts() {
        return  insuranceContractRepository.findAll();
    }




}
