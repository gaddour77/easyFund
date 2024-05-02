package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.*;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.repositories.IOfferRepositories;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class OfferServicesImpl implements IOfferServices{
 private IOfferRepositories offerRepositories;
 private IFinancingRequestRepository financingRequestRepository;

    @Override
    public Offer addOffer(Offer offer) {
        String image = offer.getOfferImage();
        try {
            URL url = new URL(image);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // Check if the response code is HTTP OK
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();

                // Extracts file name from URL
               // String fileName = image.substring(image.lastIndexOf("/") + 1);

                // Opens an output stream to save into file
                String saveDir = "http://localhost/easyFund/img/";
                FileOutputStream outputStream = new FileOutputStream(saveDir + image);
                String path = "http://localhost/easyFund/img/";
                offer.setOfferImage(path + image);
                // Reading from the input stream and writing to the output stream
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                System.out.println("Image downloaded: " + image);
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return offerRepositories.save(offer);
    }
    public Offer findById(long id){
        return offerRepositories.findById(id).orElse(null);

    }
    public List<Offer> addOffers(List<Offer> listOffers){
        return  offerRepositories.saveAll(listOffers);
    }
    public Offer updateOffer(Offer offer){
        Offer existingOffer = offerRepositories.findById(offer.getOffreId()).orElse(null);

        existingOffer.setOfferLink(offer.getOfferLink());
        existingOffer.setOfferCategory(offer.getOfferCategory());
        existingOffer.setOfferDescription(offer.getOfferDescription());
        existingOffer.setOfferPrice(offer.getOfferPrice());
        existingOffer.setOfferStatus(offer.getOfferStatus());
        return offerRepositories.save(existingOffer);

    }
    public List<Offer> findAll(){
        return offerRepositories.findAll();
    }
    public String delete( long id){
        offerRepositories.deleteById(id);
        return "offer deleted :" +id;
    }
    public List<Offer> scraped(){
        List<Offer> offerList = new ArrayList<>();
        return offerList;
    }
    public void deleteAll(){
        
        offerRepositories.deleteAll();
        System.out.println("offers deleted");
    }
   public List<Offer> getByStatus(String status){

        OfferStatus offerStatus = OfferStatus.valueOf(status);
       List<Offer> list = offerRepositories.finbByOfferStatus(offerStatus);
        return  list;
    }
    public List<Offer> addScrap(){
        List<Offer> scrapOffers = new ArrayList<>();
        //les sites (urls,balise,nom,prix)
        WebSite jumia = new WebSite("https://www.jumia.com.tn/catalog/?q=ordinateur%2Bportable","a.core","h3.name","div.prc","img._ni.camp");
        WebSite tunisianet = new WebSite("https://www.tunisianet.com.tn/301-pc-portable-tunisie","div.thumbnail-container.text-xs-center","div.wb-product-desc.product-description.col-lg-5.col-xl-5.col-md-6.col-sm-6.col-xs-6","span.price","img.center-block.img-responsive");
       WebSite myTek = new WebSite("https://www.mytek.tn/catalogsearch/result/?q=offre","li.item.product.product-item","div.prdtBILDetails","span.special-price","img.product-image-photo");

        List<WebSite> webSiteList =new ArrayList<>();
        webSiteList.add(myTek);
        webSiteList.add(jumia);
        webSiteList.add(tunisianet);

        for (WebSite webSite : webSiteList){
            try {
                Connection con = Jsoup.connect(webSite.getUrl());
                Document doc = con.get();
                //teste de connection
                if(con.response().statusCode()==200){
                    System.out.println(webSite.getUrl() + "trouvé");
                }
                Elements elements = doc.select(webSite.getBaliseName());
                if(!elements.isEmpty()){
                    for (Element row : elements){
                        String  name = row.select(webSite.getBaliseDescription()).text();
                        String  price = row.select(webSite.getBalisePrice()).text();
                      String  image =row.select(webSite.getBaliseImage()).attr("abs:src").toString();
                      if( webSite==jumia){
                          image =row.select(webSite.getBaliseImage()).attr("abs:data-src").toString();
                          if (image.isEmpty()){
                              image = row.select("img.img").attr("abs:data-src").toString();
                          }

                      }
                     //   String  image =row.getElementsByClass(webSite.getBaliseImage()).first().attr("abs:src");

                        if(name.isEmpty()){
                            System.out.println("problem balise nom ");
                        }
                        if(price.equals("")){
                            System.out.println("problem balise prix");

                        }else{
                            System.out.println("name: " + name);
                            System.out.println("price: " + price.replaceAll("[^0-9,.]", ""));
                            System.out.println( "link : "+ webSite.getUrl());
                            System.out.println("image : "+image);
                            OfferStatus offerStatus = OfferStatus.PENDING;
                            OfferCategory category = OfferCategory.TECH;
                           //partie valide
                           /*

                            String k = price.replaceAll("\\..*$", "");
                            k=k.replaceAll("[^0-9]", "");*/

                            String k = price.replaceAll("[^0-9,]", ""); // Remove all characters except digits and commas
                            k = k.replaceAll(",", ""); // Remove any remaining commas

                            int commaIndex = k.indexOf(','); // Find the index of the first comma

                            if (commaIndex != -1) { // If a comma is found
                                k = k.substring(0, commaIndex); // Keep only the part before the comma
                            }


                        Long p = Long.parseLong(k)/100;
                            if(webSite==myTek){
                                p=p/10;
                            } else if (webSite==tunisianet) {
                                p=p/1000000;
                            }

                            Offer offer = new Offer(name.substring(0, Math.min(name.length(), 100)),doc.location(),p,offerStatus,category,image);
                             offer.setOfferImage(image);
                             scrapOffers.add(offer);

                        }

                    }
                }else {
                    System.out.println("non trouvé");
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        //partie valide
        List<Offer> offerList = new ArrayList<>();
       // List<Offer> exo =new ArrayList<>();

        for(Offer offer : scrapOffers){
            System.out.println("tracking offer  2: "+offer.getOfferImage());
            Offer   existingOffer = offerRepositories.findTopByOfferCategoryAndOfferDescriptionAndOfferLink(offer.getOfferCategory(), offer.getOfferDescription(),offer.getOfferLink());
           // List<Offer>  exo = offerRepositories.findByOfferCategoryAndOfferDescriptionAndOfferLink(offer.getOfferCategory(), offer.getOfferDescription(),offer.getOfferLink());

           if(existingOffer==null){

              //offerRepositories.save(offer);
               offerList.add(offer);
               String image= offer.getOfferImage();
               //download image
               try {
                   URL url = new URL(image);
                   HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                   int responseCode = httpConn.getResponseCode();

                   // Check if the response code is HTTP OK
                   if (responseCode == HttpURLConnection.HTTP_OK) {
                       // Opens input stream from the HTTP connection
                       InputStream inputStream = httpConn.getInputStream();

                       // Extracts file name from URL
                       String fileName = image.substring(image.lastIndexOf("/") + 1);

                       // Opens an output stream to save into file
                       String saveDir = "C:/xampp/htdocs/easyFund/img/";
                       FileOutputStream outputStream = new FileOutputStream(saveDir + fileName);
                       String path = "http://localhost/easyFund/img/";
                       offer.setOfferImage(path + fileName);
                       // Reading from the input stream and writing to the output stream
                       int bytesRead;
                       byte[] buffer = new byte[4096];
                       while ((bytesRead = inputStream.read(buffer)) != -1) {
                           outputStream.write(buffer, 0, bytesRead);
                       }

                       outputStream.close();
                       inputStream.close();

                       System.out.println("Image downloaded: " + fileName);
                   } else {
                       System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                   }
                   httpConn.disconnect();
               } catch (Exception e) {
                   e.printStackTrace();
               }
               //end partie download image


           }else if(existingOffer.getOfferPrice()!=offer.getOfferPrice()){
               existingOffer.setOfferPrice(offer.getOfferPrice());
               offerRepositories.save(existingOffer);
           }

        }
               return offerRepositories.saveAll(offerList);
    }

    public FinancingRequest affecter(Long idO , Long fR){
        Offer offer = offerRepositories.findById(idO).orElse(null);
        FinancingRequest financingRequest = financingRequestRepository.findById(fR).orElse(null);
        financingRequest.setOffer(offer);
        return financingRequestRepository.save(financingRequest);
    }
    public List<FinancingRequest> financingRequests(Long id){
        Offer offer = offerRepositories.findById(id).orElse(null);
        List<FinancingRequest> financingRequests = financingRequestRepository.findByOffer(offer.getOffreId());
       return financingRequests;
    }
    public Offer approve(Long id,OfferStatus status){
        Offer offer1 = offerRepositories.findById(id).orElse(null);
        //OfferStatus offerStatus = OfferStatus.valueOf(action.toUpperCase());
       // System.out.println(offerStatus);
        offer1.setOfferStatus(status);
        return offerRepositories.save(offer1);
    }
}
