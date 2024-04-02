package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;

import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.services.MicroCreditServicesImpl;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(path = "credit")
@RestController
public class MicroCreditController {

    @Autowired
    private MicroCreditServicesImpl microCreditService;
    @PostMapping(path = "createCredit")
    public ResponseEntity<MicroCredit> createMicroCredit(@RequestBody MicroCredit microCredit) {
        MicroCredit credit = microCreditService.createMicroCredit(microCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credit); // Created return 201
    }

    @GetMapping("getCredit/{id}")
    public ResponseEntity<MicroCredit> getCreditById(@PathVariable("id") long id) {
        MicroCredit credit = microCreditService.findCreditById(id);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credit); // get successfully return 200
    }

    @GetMapping("getAllCredits")
    public ResponseEntity<List<MicroCredit>> getAllCredits() {
        List<MicroCredit> credits = microCreditService.findAllCredits();
        if (credits == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        return ResponseEntity.ok(credits);
    }

    @DeleteMapping("deleteCredit/{id}")
    public ResponseEntity<?> deleteCredit(@PathVariable("id") Long idCredit) {

        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Credit not found, return 404
        }
        microCreditService.deleteCredit(idCredit);
        return ResponseEntity.ok().build(); // Credit deleted successfully, return 200
    }

    //
    @PutMapping("updateCredit/{idCredit}")
    public ResponseEntity<MicroCredit> updateCredit(@PathVariable Long idCredit, @RequestBody MicroCredit updatedCredit) {
        if (!idCredit.equals(updatedCredit.getMicroCreditId())) {
            return ResponseEntity.badRequest().build();
        }
        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build(); // Updated deleted successfully, return 200
        }
        MicroCredit updatedCreditEntity = microCreditService.updateCredit(updatedCredit);
        return ResponseEntity.ok(updatedCreditEntity);
    }

    @GetMapping("getCreditsByStatus/{status}")
    public ResponseEntity<List<MicroCredit>> getCreditsByStatus(@PathVariable("status") CreditStatus creditStatus) {
        List<MicroCredit> credits = microCreditService.getCreditsByStatus(creditStatus);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200

    }

    @GetMapping("getCreditsByType/{type}")
    public ResponseEntity<List<MicroCredit>> getCreditsByType(@PathVariable("type") CreditType creditType) {
        List<MicroCredit> credits = microCreditService.getCreditsByType(creditType);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200
    }

    @PutMapping("updateCreditStatus/{idCredit}/{status}")
    public ResponseEntity<MicroCredit> updateCreditStatus(@PathVariable("idCredit") Long idCredit, @PathVariable("status") CreditStatus status) {
        MicroCredit credit = microCreditService.findCreditById(idCredit);
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }
        MicroCredit updatedCredit = microCreditService.updateStatus(idCredit, status); // Credit updated successfully, return 200
        return ResponseEntity.ok(updatedCredit);
    }

    @GetMapping("getCreditByAccount/{idAccount}")
    public ResponseEntity<List<MicroCredit>> getCreditByAccount(@PathVariable("idAccount") Long id) {
        List<MicroCredit> credits = microCreditService.getCreditByAccountId(id);
        if (credits == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credits); // get successfully, return 200
    }

    @GetMapping("simulator/{amount}/{period}/{typePeriod}")
    public ResponseEntity<List<Object>> Simulation(@PathVariable double amount, @PathVariable int period,@PathVariable String typePeriod ){
        if (amount <= 1000 || period <= 1 || typePeriod == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Object> simulation = microCreditService.Simulation(amount, period, typePeriod);
        return ResponseEntity.ok(simulation);
    }

    @GetMapping("FailureToPay/{idCredit}/{period}/{interestAmount}")
    public List<Object> FailureToPay(@PathVariable long idCredit, @PathVariable int period, @PathVariable double interestAmount){
        return microCreditService.FailureToPay(idCredit,period,interestAmount);
    }


    @GetMapping("/excel/{amount}/{period}/{typePeriod}")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response, @PathVariable double amount, @PathVariable int period, @PathVariable String typePeriod) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CreditSimulation_" + amount + "_" + typePeriod + "_" + period + ".xlsx";
        response.setHeader(headerKey, headerValue);
        ResponseEntity<List<Object>> simulationResponse = Simulation(amount, period, typePeriod);

        if (simulationResponse.getStatusCode() == HttpStatus.OK) {
            List<Object> simulation = simulationResponse.getBody();
            CreditExcelExporter excelExporter = new CreditExcelExporter(simulation);
            excelExporter.exportExcel(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pdf/{amount}/{period}/{typePeriod}")
    public ResponseEntity<?> exportToPDF(HttpServletResponse response, @PathVariable double amount, @PathVariable int period, @PathVariable String typePeriod) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=CreditSimulation_" + amount + "_" + typePeriod + "_" + period + ".pdf";
        response.setHeader(headerKey, headerValue);

        ResponseEntity<List<Object>> simulationResponse = Simulation(amount, period, typePeriod);

        if (simulationResponse.getStatusCode() == HttpStatus.OK) {
            List<Object> simulation = simulationResponse.getBody();
            CreditExcelExporter pdfExporter = new CreditExcelExporter(simulation);
            pdfExporter.exportPDF(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


}


