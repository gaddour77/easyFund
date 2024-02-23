package tn.esprit.easyfund.controllers;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.InvestementRequest;
import tn.esprit.easyfund.services.InvestementRequest.IInvestementRequestService;
import tn.esprit.easyfund.services.InvestementRequest.InvestementRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/InvestementRequest")
public class InvestementRequestController
{
    @Autowired
    InvestementRequestService investementRequestService;
    @PostMapping("/add-InvestementRequest")
    @ResponseBody
    public InvestementRequest addInvestementRequest(@RequestBody InvestementRequest b) {

        return investementRequestService.add(b);
    }
    @GetMapping("/get_all_InvestementRequests")
    public List<InvestementRequest> findAll() {
        return investementRequestService.findAll();
    }

    @PutMapping("/updateInvestementRequest/{id}")
    public InvestementRequest update(@RequestBody InvestementRequest InvestementRequest ,@PathVariable("id") Integer id) {
        return investementRequestService.update(id,InvestementRequest);
    }

    @DeleteMapping("/deleteInvestementRequest/{InvestementRequestId}")
    public void delete(@PathVariable("InvestementRequestId") Integer InvestementRequestId) {
        investementRequestService.delete(InvestementRequestId);
    }
}
