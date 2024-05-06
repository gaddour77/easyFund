package tn.esprit.easyfund.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.InsuranceContract;
import tn.esprit.easyfund.services.IInsuranceContractService;



@RestController
 @RequestMapping("/api/insuranceContracts")
    public class InsuranceContractController {

        @Autowired
        private IInsuranceContractService insuranceContractService;


        @GetMapping("/{insuranceContractId}")
        public ResponseEntity<InsuranceContract> getInsuranceContractById(@PathVariable Long insuranceContractId) {
            InsuranceContract insuranceContract = insuranceContractService.getInsuranceContractById(insuranceContractId);
            return ResponseEntity.ok(insuranceContract);
        }

        @GetMapping
        public ResponseEntity<List<InsuranceContract>> getAllInsurancesContracts() {
            List<InsuranceContract>   insuranceContract = insuranceContractService.getAllInsurancesContracts();
            return ResponseEntity.ok(insuranceContract);
        }


        @ResponseBody
        @PostMapping("/add")
        public ResponseEntity<InsuranceContract> saveInsuranceContract(@RequestBody InsuranceContract insuranceContract,  @RequestParam(name = "id") Long insuranceId) {
            InsuranceContract savedInsuranceContract = insuranceContractService.saveInsuranceContract(insuranceContract,insuranceId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInsuranceContract);
        }

        @PutMapping("/{insuranceContractId}")
        public ResponseEntity<InsuranceContract> updateInsuranceContract(@PathVariable Long insuranceContractId, @RequestBody InsuranceContract updatedInsuranceContract) {
            InsuranceContract updatedInsuranceContractData = insuranceContractService.updateInsuranceContract(insuranceContractId, updatedInsuranceContract);

            if (updatedInsuranceContractData != null) {
                return ResponseEntity.ok(updatedInsuranceContractData);
            } else {
                // Handle insuranceContract not found
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/delete")
        public ResponseEntity<Void> deleteInsuranceContract( @RequestParam(name = "id") Long insuranceContractId) {
            insuranceContractService.deleteInsuranceContract(insuranceContractId);
            return ResponseEntity.noContent().build();
        }


        @GetMapping("/get/assigned-to-agent")
        public List<InsuranceContract> getInsuranceContractsAssignedToAgent() {
            try {
                // Call the service method to get claims assigned to the agent
                return insuranceContractService.getInsuranceContractsAssignedToAgent();
            } catch (Exception e) {
                // Handle exceptions and return an appropriate response
                return (List<InsuranceContract>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

    }


