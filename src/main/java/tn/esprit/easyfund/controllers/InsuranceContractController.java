package tn.esprit.easyfund.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.easyfund.entities.Insurance;
import tn.esprit.easyfund.entities.InsuranceContract;
import tn.esprit.easyfund.services.IInsuranceContractService;


@RestController
    @RequestMapping("/api/insuranceContracts")
    public class InsuranceContractController {

        @Autowired
        private IInsuranceContractService insuranceContractService;

        @GetMapping("/getinsuranceContractsById/{insuranceContractId}")
        public InsuranceContract InsuranceContractById(@PathVariable long id){
            return insuranceContractService.getInsuranceContractById(id);
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

        @DeleteMapping
        public ResponseEntity<Void> deleteInsuranceContract( @RequestParam(name = "id") Long insuranceContractId) {
            insuranceContractService.deleteInsuranceContract(insuranceContractId);
            return ResponseEntity.noContent().build();
        }



    }


