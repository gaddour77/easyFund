package tn.esprit.easyfund.services;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;




import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.FinancingRequest;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.RequestStatus;

import tn.esprit.easyfund.entities.User;

import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.repositories.IOfferRepositories;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.List;

@AllArgsConstructor
@Service
public class FinancingRequestServicesImpl implements IFinancingRequestServices{
    private IFinancingRequestRepository financingRequestRepository;
    private IOfferRepositories offerRepositories;

    public FinancingRequest addFinancing(FinancingRequest financingRequest){


           return financingRequestRepository.save(financingRequest);

    }
    public User findUser(Long id){
        return financingRequestRepository.findUserByFinancingRequestId(id);

    }
    public List<FinancingRequest> detect(FinancingRequest fr){
        return financingRequestRepository.findByUserIdAndOfferId(fr.getUser().getUserId(),fr.getOffer().getOffreId());
    }
    public List<FinancingRequest> addFinancings(List<FinancingRequest> requests){
        return financingRequestRepository.saveAll(requests);
    }
    public FinancingRequest findById(long id){
        return financingRequestRepository.findById(id).orElse(null);
    }
    public List<FinancingRequest> findAll(){
        return financingRequestRepository.findAll();
    }
    public List<FinancingRequest>  findByOffer (long id){
        return financingRequestRepository.findByOffer(id);
    }
    public FinancingRequest update(FinancingRequest financingRequest){
        FinancingRequest currentRequest = financingRequestRepository.findById(financingRequest.getFinancingRequestId()).orElse(null);
        currentRequest.setDateFinancingRequest(financingRequest.getDateFinancingRequest());
        currentRequest.setRequestStatus(financingRequest.getRequestStatus());
        currentRequest.setFinalDate(financingRequest.getFinalDate());
        return financingRequestRepository.save(currentRequest);

    }
    public String delete(long id){
        financingRequestRepository.deleteById(id);
        return "financing request deleted :"+id;
    }
    public List<FinancingRequest> findByUser(long id){
        return financingRequestRepository.findByUser(id);
    }
 
    public ServletOutputStream calculateAmortizationSchedule(FinancingRequest financingRequest,HttpServletResponse response,String name) throws IOException, InterruptedException {
       // CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet feuille = workbook.createSheet("AmortizationSchedule");

        // Création des en-têtes
       HSSFRow enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Month");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");
        Offer offer = offerRepositories.findById(financingRequest.getOffer().getOffreId()).orElse(null);
        LocalDate dateDebut = financingRequest.getDateFinancingRequest();
        LocalDate dateFin = financingRequest.getFinalDate();

        double tauxInteretAnnuel = 5.6;
        // Calcul de la période entre les deux dates
        Period period = Period.between(dateDebut, dateFin);
        System.out.println("period : "+period);
        int moisDifference = period.getYears() * 12 + period.getMonths();
        double soldeInitial = offer.getOfferPrice();
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double paiementMensuel = offer.getOfferPrice() * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -moisDifference)));
             System.out.println("id :"+paiementMensuel+tauxInteretAnnuel+tauxInteretMensuel+"mois :"+moisDifference+"dated"+dateDebut);
        for (int mois = 1; mois <= moisDifference; mois++) {
            Row ligne = feuille.createRow(mois);
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal = soldeInitial - principal;
            System.out.println("indicateur : "+mois+financingRequest.toString()+mois+interets+principal+soldeFinal);

            ligne.createCell(0).setCellValue(mois);
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(/*principal*/paiementMensuel);
            ligne.createCell(4).setCellValue(/*paiementMensuel*/principal);
            ligne.createCell(5).setCellValue(soldeFinal);

            soldeInitial = soldeFinal;
            if(soldeFinal<0){
                soldeFinal=0 ;
            }
        }
       String uploadDir="C:/Users/GADOUR/IdeaProjects/easyFund/src/main/resources/excel";
        String  fileName = "";
        File file = new File(uploadDir + File.separator + name);
        FileOutputStream fs = new FileOutputStream(file);
        ServletOutputStream ops = response.getOutputStream();
       // fs.wait(2000);

         workbook.write(fs);

        workbook.write(ops);
        //workbook.write(file);


        workbook.close();
        ops.close();
        fs.close();
        return ops;
    }

    public void calculateAmortizationSchedule1(FinancingRequest financingRequest,HttpServletResponse response,String name) throws IOException, InterruptedException {
        // CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet feuille = workbook.createSheet("AmortizationSchedule");

        // Création des en-têtes
        HSSFRow enTete = feuille.createRow(0);
        enTete.createCell(0).setCellValue("Month");
        enTete.createCell(1).setCellValue("Solde initial");
        enTete.createCell(2).setCellValue("Intérêts");
        enTete.createCell(3).setCellValue("Principal");
        enTete.createCell(4).setCellValue("Paiement mensuel");
        enTete.createCell(5).setCellValue("Solde final");
        enTete.createCell(6).setCellValue("status");

        Offer offer = offerRepositories.findById(financingRequest.getOffer().getOffreId()).orElse(null);
        LocalDate dateDebut = financingRequest.getDateFinancingRequest();
        LocalDate dateFin = financingRequest.getFinalDate();

        double tauxInteretAnnuel = 5;
        // Calcul de la période entre les deux dates
        Period period = Period.between(dateDebut, dateFin);
        System.out.println("period : "+period);
        int moisDifference = period.getYears() * 12 + period.getMonths();

        double soldeInitial = offer.getOfferPrice();
        //taux d'interet
        if(moisDifference>12){
            int moissupp = moisDifference -12;
            tauxInteretAnnuel += 0.1 *moissupp;
        }
        if (soldeInitial>1000){
            double diff = tauxInteretAnnuel -1000;
            int nb =(int) diff/300;
            tauxInteretAnnuel += 0.3*nb;

        }
        System.out.println("taux d'intert:   "+tauxInteretAnnuel);
        double tauxInteretMensuel = tauxInteretAnnuel / 12 / 100;
        double paiementMensuel = offer.getOfferPrice() * (tauxInteretMensuel / (1 - Math.pow(1 + tauxInteretMensuel, -moisDifference)));

          System.out.println("id :"+paiementMensuel+tauxInteretAnnuel+tauxInteretMensuel+"mois :"+moisDifference+"dated"+dateDebut);


        for (int mois = 1; mois <= moisDifference; mois++) {
            Row ligne = feuille.createRow(mois);
            LocalDate dateRemboursement = dateDebut.plusMonths(mois);
            double interets = soldeInitial * tauxInteretMensuel;
            double principal = paiementMensuel - interets;
            double soldeFinal = soldeInitial - principal;
            System.out.println("indicateur : "+mois+financingRequest.toString()+mois+interets+principal+soldeFinal);

            ligne.createCell(0).setCellValue(String.valueOf(dateRemboursement));
            ligne.createCell(1).setCellValue(soldeInitial);
            ligne.createCell(2).setCellValue(interets);
            ligne.createCell(3).setCellValue(/*principal*/paiementMensuel);
            ligne.createCell(4).setCellValue(/*paiementMensuel*/principal);
            ligne.createCell(5).setCellValue(soldeFinal);
            ligne.createCell(6).setCellValue("encore");

            soldeInitial = soldeFinal;
            if(soldeFinal<0){
                soldeFinal=0 ;
            }
        }

        String uploadDir="C:/xampp/htdocs/easyFund/excel";

        String  fileName = "";
        File file = new File(uploadDir + File.separator + name);
        FileOutputStream fs = new FileOutputStream(file);
        ServletOutputStream ops = response.getOutputStream();
        // fs.wait(2000);

        workbook.write(fs);


        //workbook.write(file);


        workbook.close();
       // ops.close();
        fs.close();

    }
    public void saveOpsToFile(ServletOutputStream ops, String fileName) throws IOException {

        String uploadDir="C:/Users/GADOUR/IdeaProjects/easyFund/src/main/resources/excel";
        String path =uploadDir+"/"+fileName;
        // Create a ByteArrayOutputStream to capture the data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create a custom OutputStream that writes to both the ServletOutputStream and the ByteArrayOutputStream
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                ops.write(b); // Write to ServletOutputStream
                baos.write(b); // Write to ByteArrayOutputStream

            }
        };
        // Wrap the custom OutputStream in a BufferedOutputStream for performance
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

        // Create a File object for the destination file
        File file = new File(uploadDir + File.separator + fileName);

        try {
            // Write the captured data to the file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                baos.writeTo(fos); // Write the captured data to the file
            }
        } finally {
            // Close the streams
            bufferedOutputStream.close();
            baos.close();
            ops.close();
        }
    }
    public double installmentPayment(long id) throws IOException {
        FinancingRequest financingRequest =financingRequestRepository.findById(id).orElse(null);

        String uploadDir="C:/xampp/htdocs/easyFund/excel/";
         String path = uploadDir+ financingRequest.getExcel();

        double amount =0;
         if (path!=null & financingRequest!=null){
             POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
                     HSSFWorkbook wb = new HSSFWorkbook(fs);
             HSSFSheet sheet = wb.getSheetAt(0);
             HSSFRow row = null;
             HSSFCell cell = null;
             HSSFCell cellamount =null;
             String status ="";
             int rowNum =sheet.getLastRowNum();
             int i=1;

             boolean test =false;
             while (i<=rowNum & test==false){
                 row = sheet.getRow(i);
                 cell = row.getCell((short) 6);

                 status = cell.getStringCellValue();
                 test=status.equals("encore");
                 if(test){
                     cell.setCellValue(new HSSFRichTextString("payer"));
                     cellamount = row.getCell((short) 4);
                     amount = cellamount.getNumericCellValue();
                 }
                 i++;
             }


             /*HSSFSheet sheet = wb.getSheetAt(0);
             HSSFRow row = sheet.getRow(1);
             sheet.removeRow(row);
             int lastRowNum = sheet.getLastRowNum();
             for (int i = 2; i <= lastRowNum; i++) {
                 sheet.shiftRows(i, lastRowNum, -1);
             }*/


             FileOutputStream fileOut = new FileOutputStream(path);
             wb.write(fileOut);
             fileOut.close();
             wb.close();

         }
         return amount;
    }

    public void generateExcel(HttpServletResponse response) throws Exception {

        List<FinancingRequest> courses = financingRequestRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Courses Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("date debut");
        row.createCell(2).setCellValue("date fin");

        int dataRowIndex = 1;

        for (FinancingRequest course : courses) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(course.getFinancingRequestId());
            dataRow.createCell(1).setCellValue(course.getDateFinancingRequest().format(DateTimeFormatter.ofPattern("DD-MM-YYYY")));
            dataRow.createCell(2).setCellValue(course.getFinalDate().format(DateTimeFormatter.ofPattern("DD-MM-YYYY")));
            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();

    }
    public List<FinancingRequest> finbByStatus(RequestStatus status){
        return financingRequestRepository.findByRequestStatus(status);

    }

    public FinancingRequest approve(Long id,String status){
      FinancingRequest financingRequest =  financingRequestRepository.findById(id).orElse(null);
      RequestStatus requestStatus =RequestStatus.valueOf(status.toUpperCase());
      financingRequest.setRequestStatus(requestStatus);
      return financingRequestRepository.save(financingRequest);
    }


}
