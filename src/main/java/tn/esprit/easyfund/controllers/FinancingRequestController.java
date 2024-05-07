package tn.esprit.easyfund.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.RequestStatus;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.services.AuthenticationService;
import tn.esprit.easyfund.services.FinancingRequestServicesImpl;
import tn.esprit.easyfund.services.OfferServicesImpl;
import tn.esprit.easyfund.services.SmsServicesImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/financingrequest")
public class FinancingRequestController {
    private FinancingRequestServicesImpl financingRequestServices;
    private OfferServicesImpl offerServices;
    private AuthenticationService authenticationService;
    private IUserRepository userRepository;
    private SmsServicesImpl smsServices;
    private final Path rootLocation = Paths.get(" resources/excel/");

    @PostMapping("/addfinancing")
    public ResponseEntity<FinancingRequest> addfinancing(@RequestBody FinancingRequest financingRequest,HttpServletResponse response) throws Exception {

         Long id = authenticationService.getConnectedUser();
          User user = userRepository.findById(id).orElse(null);
          if(user!=null){
              financingRequest.setUser(user);

          }
          financingRequest.setRequestStatus(RequestStatus.PENDING);
        ////test
        List<FinancingRequest> lfr = financingRequestServices.detect(financingRequest);
        for( FinancingRequest f : lfr){
            System.out.println("its here :"+f.getExcel()+f.getFinalDate());
        }
        //String uploadDir="src/main/resources/excel/";
        String name = financingRequest.getUser().getUserId()+"easy"+financingRequest.getOffer().getOffreId()+".xls";
       // String excel = uploadDir+name;
        String excel = name;
        System.out.println(excel);
        financingRequest.setExcel(excel);
        if (lfr.size()==0){
            financingRequestServices.calculateAmortizationSchedule1(financingRequest,response,name);
            FinancingRequest savedRequest = financingRequestServices.addFinancing(financingRequest);
            return new ResponseEntity<>(savedRequest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Or any other appropriate response code
        }





    }
    @GetMapping("/finduser/{id}")
    public Long findUserId(@PathVariable Long id){
        User user = financingRequestServices.findUser(id);
        if (user!=null){
            return user.getUserId();

        }
        return null;
    }
    @GetMapping("/downloadExcel/{name}")
    public ResponseEntity<Resource> downloadExcelFile(@PathVariable String name) throws IOException {
        //test
        String uploadDir="src/main/resources/excel/";
        String ecxel = "C:/xampp/htdocs/easyFund/excel/"+name;
        System.out.println(ecxel);

       /* File file = ResourceUtils.getFile(ecxel);
        File file1 = new ClassPathResource(ecxel).getFile();
        FileInputStream fileInputStream1 = new FileInputStream(file1);*/
        //
        Path path = Paths.get(ecxel);
        System.out.println(ecxel);
        FileInputStream fileInputStream = new FileInputStream(path.toFile());

        InputStreamResource resource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + path.getFileName().toString())
                .body(resource);
    }
    @GetMapping("/downloadExcel11/{id}")
    public ResponseEntity<Resource> downloadExcelFile1(@PathVariable Long id) throws IOException {
        FinancingRequest fr = financingRequestServices.findById(id);

        //test
        String name =fr.getExcel();
        String uploadDir="src/main/resources/excel/";
        String ecxel = "C:/xampp/htdocs/easyFund/excel/"+name;
        System.out.println(ecxel);


       /* File file = ResourceUtils.getFile(ecxel);
        File file1 = new ClassPathResource(ecxel).getFile();
        FileInputStream fileInputStream1 = new FileInputStream(file1);*/
        //
        Path path = Paths.get(ecxel);
        System.out.println(ecxel);
        FileInputStream fileInputStream = new FileInputStream(path.toFile());

        InputStreamResource resource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + path.getFileName().toString())
                .body(resource);
    }
    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        System.out.println("api consommé");
        System.out.println("app/src/main/resources/excel/"+filename);
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                System.out.println("file detected");
                return ResponseEntity.ok().body(resource);
            } else {
                System.out.println("file non detected");
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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
        System.out.println(id);
        return financingRequestServices.findByOffer(id);
    }
    @GetMapping("/findbyuser/{id}")
    public List<FinancingRequest> findByUser(@PathVariable  long id){
        return financingRequestServices.findByUser(id);
    }
    @GetMapping("/mylist")
    public List<FinancingRequest> findMyfinancings(){
        Long id = authenticationService.getConnectedUser();
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
    @GetMapping("/payment")
    public void installmentPayment(long id) throws IOException{
        financingRequestServices.installmentPayment(id);
    }
    @GetMapping("/excel")
    public void generateExcelReport(HttpServletResponse response) throws Exception{
        System.out.println("hi /ecxel");
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=courses.xls";

        response.setHeader(headerKey, headerValue);

        financingRequestServices.generateExcel(response);

        response.flushBuffer();
    }
    @GetMapping("/findByStatus/{status}")
    public List<FinancingRequest> findByStatus(@PathVariable String status){
        System.out.println("salut");
        RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
        return financingRequestServices.finbByStatus(requestStatus);
    }
    @PutMapping("/descision/{id}/{status}/{idU}")
    public FinancingRequest approve(@PathVariable Long id, @PathVariable  String status,@PathVariable Long idU){

        System.out.println(id);
        FinancingRequest financingRequest = financingRequestServices.approve(id,status);
        User user = userRepository.findById(idU).orElse(null);

        String message = "Dear Customer " + user.getFirstname() + ", your Financing Request No." + id + " has been " + status.toUpperCase() + ".";

        System.out.println(message);
        smsServices.sendSms(user.getPhoneNumber(),message);

        return financingRequest;
    }
}
