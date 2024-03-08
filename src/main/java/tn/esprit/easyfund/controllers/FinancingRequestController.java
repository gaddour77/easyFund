package tn.esprit.easyfund.controllers;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.services.FinancingRequestServicesImpl;
import tn.esprit.easyfund.services.IFinancingRequestServices;
import tn.esprit.easyfund.services.OfferServicesImpl;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/financingrequest")
public class FinancingRequestController {
    private FinancingRequestServicesImpl financingRequestServices;
    private OfferServicesImpl offerServices;

    @PostMapping("/addfinancing")
    public FinancingRequest addfinancing(@RequestBody FinancingRequest financingRequest,HttpServletResponse response) throws Exception {
///partie valid
        /*try{
            response.setContentType("application/octet-stream");

            String headerKey = "Content-Disposition";
            String name = financingRequest.getUser().getNom()+"easy"+financingRequest.getOffer().getOffreId()+".xls";
            String headerValue = "attachment;filename="+name;

            response.setHeader(headerKey, headerValue);
            ServletOutputStream ops = financingRequestServices.calculateAmortizationSchedule(financingRequest, response,name);

            financingRequestServices.saveOpsToFile(ops,name);
        }catch (IOException exception){
           exception.printStackTrace();
        }

        return financingRequestServices.addFinancing(financingRequest);
        */
        ////test
        String uploadDir="C:/Users/GADOUR/IdeaProjects/easyFund/src/main/resources/excel/";
        String name = financingRequest.getUser().getNom()+"easy"+financingRequest.getOffer().getOffreId()+".xls";
          financingRequestServices.calculateAmortizationSchedule1(financingRequest,response,name);
          String excel = uploadDir+name;
          System.out.println(excel);
          financingRequest.setExcel(excel);


        return financingRequestServices.addFinancing(financingRequest);

    }
    @PostMapping("/addfinancings")
    public List<FinancingRequest> addfinancings(List<FinancingRequest> financingRequests){
        return financingRequestServices.addFinancings(financingRequests);
    }
    @GetMapping("/findfinancing/{id}")
    public FinancingRequest findById(@PathVariable long id){
        return financingRequestServices.findById(id);
    }
    @GetMapping("/findall")
    public List<FinancingRequest> findfinancings(){
        return financingRequestServices.findAll();
    }
    @GetMapping("/findbyoffer/{id}")
    public List<FinancingRequest> findByOffer(@PathVariable long id){
        return financingRequestServices.findByOffer(id);
    }
    @GetMapping("/findbyuser/{id}")
    public List<FinancingRequest> findByUser(long id){
        return financingRequestServices.findByUser(id);
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id){
        return financingRequestServices.delete(id);
    }
    @PutMapping("/update")
    public FinancingRequest update(@RequestBody FinancingRequest financingRequest){
        return financingRequestServices.update(financingRequest);
    }
    @GetMapping("/excel")
    public void generateExcelReport(HttpServletResponse response) throws Exception{

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=courses.xls";

        response.setHeader(headerKey, headerValue);

        financingRequestServices.generateExcel(response);

        response.flushBuffer();
    }
}
