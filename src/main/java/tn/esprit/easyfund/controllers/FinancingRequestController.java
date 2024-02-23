package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.services.IFinancingRequestServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/financingrequest")
public class FinancingRequestController {
    private IFinancingRequestServices financingRequestServices;
    @PostMapping("/addfinancing")
    public FinancingRequest addfinancing(@RequestBody FinancingRequest financingRequest){
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
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id){
        return financingRequestServices.delete(id);
    }
    @PutMapping("/update")
    public FinancingRequest update(@RequestBody FinancingRequest financingRequest){
        return financingRequestServices.update(financingRequest);
    }
}
