package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Insurance;
import tn.esprit.easyfund.services.IInsuranceService;

import java.util.List;


@RestController
    @RequestMapping("/api/insurances")
    public class InsuranceController {

        @Autowired
        private IInsuranceService insuranceService;
   

        @GetMapping("/{insuranceId}")
        public ResponseEntity<Insurance> getInsuranceById(@PathVariable Long insuranceId) {
            Insurance insurance = insuranceService.getInsuranceById(insuranceId);
            return ResponseEntity.ok(insurance);
        }

        @GetMapping
        public ResponseEntity<List<Insurance>> getAllInsurances() {
            List<Insurance> insurances = insuranceService.getAllInsurances();
            return ResponseEntity.ok(insurances);
        }

        @PostMapping
        public ResponseEntity<Insurance> saveInsurance(@RequestBody Insurance insurance) {
            Insurance savedInsurance = insuranceService.saveInsurance(insurance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedInsurance);
        }
        @PutMapping("/{insuranceId}")
        public ResponseEntity<Insurance> updateInsurance(@PathVariable Long insuranceId, @RequestBody Insurance updatedInsurance) {
            Insurance updatedInsuranceData = insuranceService.updateInsurance(insuranceId, updatedInsurance);

            if (updatedInsuranceData != null) {
                return ResponseEntity.ok(updatedInsuranceData);
            } else {
                // Handle insurance not found
                return ResponseEntity.notFound().build();
            }
        }

        @DeleteMapping("/{insuranceId}")
        public ResponseEntity<Void> deleteInsurance(@PathVariable Long insuranceId) {
            insuranceService.deleteInsurance(insuranceId);
            return ResponseEntity.noContent().build();
        }

   


    }


