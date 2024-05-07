package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.ElementInfo;
import tn.esprit.easyfund.repositories.IInfoRepository;
import tn.esprit.easyfund.services.InfoServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/info")
@CrossOrigin(origins = "https://localhost:4200")
public class InfoController {
    private final InfoServices infoServices;
    private IInfoRepository elementInfoRepository;

    @PostMapping("/save")
    public ResponseEntity<Void> saveElementInfo(@RequestBody List<ElementInfo> elementInfoList) {
        for (ElementInfo elementInfo : elementInfoList) {
            System.out.println("l'id est :"+elementInfo.getId());
            // Check if an ElementInfo with the same elementId exists in the database
            ElementInfo existingElementInfo = elementInfoRepository.findById(elementInfo.getId()).orElse(null);
            if (existingElementInfo != null) {
                // If exists, update the time
                existingElementInfo.setTime(existingElementInfo.getTime() + elementInfo.getTime());
                elementInfoRepository.save(existingElementInfo);
            } else {
                // If not exists, save the new ElementInfo
                elementInfo.setId(elementInfo.getId());
                elementInfoRepository.save(elementInfo);
            }
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/get")
    public List<ElementInfo> getAll(){
        for(ElementInfo info : elementInfoRepository.findAll()){
            System.out.println("data :"+info.getTime());
        }
        return elementInfoRepository.findAll();
    }
}
