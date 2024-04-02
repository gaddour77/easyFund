package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.Insurance;
import tn.esprit.easyfund.entities.User;

import tn.esprit.easyfund.entities.InsuranceContract;
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
    public InsuranceContract saveInsuranceContract(InsuranceContract insuranceContract, Long insuranceId) {
        // Add any additional business logic if needed
    
        // Get the current user details from the security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        // Find the insurance based on the provided insuranceId
        Insurance insurance = insuranceService.getInsuranceById(insuranceId);
    
        // Find the user based on the authenticated user's email
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    
        // Set the user for the insurance contract
        insuranceContract.setUser(user);
        insuranceContract.setUserId(user.getUserId());
    
        // Get agents with low workload
        List<User> agents = userRepository.findAllAgentWithWorkload();
        if (!agents.isEmpty()) {
            User agent = agents.get(0);
            insuranceContract.setUser(agent);
            insuranceContract.setAgentId(agent.getUserId());
        }
    
        // Set the insurance for the insurance contract
        insuranceContract.setInsurance(insurance);
        insuranceContract.setInsuranceId(insurance.getId());
        
        // Save and return the insurance contract
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



       @Override
    public List<InsuranceContract> getInsuranceContractsAssignedToAgent() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Ensure the user is authenticated and has a valid principal
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Retrieve the currently authenticated agent
            User agent = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            // Retrieve claims assigned to the agent
            return insuranceContractRepository.findByAgent(agent);
        } else {
            // Handle the case where the user is not authenticated or has an invalid principal
            throw new RuntimeException("Invalid authentication or principal");
        }
    }





}
