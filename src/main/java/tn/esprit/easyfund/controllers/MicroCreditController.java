package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.services.implementations.MicroCreditServicesImpl; // Import the interface instead of the implementation

import java.util.List;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/credit")
public class MicroCreditController {

    @Autowired
    private MicroCreditServicesImpl microCreditServices; // Inject the interface

    @PostMapping("/addCredit")
    public void addCredit(@RequestBody MicroCredit microCredit)
    {
        microCreditServices.addCredit(microCredit);
    }

    @GetMapping("getCredit/{id}")
    public MicroCredit getCreditById(@PathVariable("id") long id)
    {
        return microCreditServices.findCreditById(id);
    }

    @PutMapping("updateCredit")
    public MicroCredit updateCredit(@RequestBody MicroCredit credit)
    {
        return microCreditServices.updateCredit(credit);
    }

    @PostMapping("getCredit/{id}")
    public MicroCredit UpdateCreditStatus(@PathVariable("id") long id)
    {
        return microCreditServices.updateStatus(id);
    }
}
