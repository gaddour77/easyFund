package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.*;
import tn.esprit.easyfund.repositories.IFinancingRequestRepository;
import tn.esprit.easyfund.repositories.IOfferRepositories;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class OfferServicesImpl implements IOfferServices{
 private IOfferRepositories offerRepositories;
 private IFinancingRequestRepository financingRequestRepository;

    @Override
    public Offer addOffer(Offer offer) {
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
    public List<Offer> addScrap(){
        List<Offer> scrapOffers = new ArrayList<>();
        //les sites (urls,balise,nom,prix)
        WebSite jumia = new WebSite("https://www.jumia.com.tn/catalog/?q=ordinateur%2Bportable","a.core","h3.name","div.prc");
        WebSite tunisianet = new WebSite("https://www.tunisianet.com.tn/301-pc-portable-tunisie","div.thumbnail-container.text-xs-center","div.wb-product-desc.product-description.col-lg-5.col-xl-5.col-md-6.col-sm-6.col-xs-6","span.price");
       WebSite myTek = new WebSite("https://www.mytek.tn/catalogsearch/result/?q=offre","li.item.product.product-item","div.prdtBILDetails","span.special-price");

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
                        String  price =row.select(webSite.getBalisePrice()).first().text();

                        if(name.isEmpty()){
                            System.out.println("problem balise nom ");
                        }
                        if(price.equals("")){
                            System.out.println("problem balise prix");

                        }else{
                            System.out.println("name: " + name);
                            System.out.println("price: " + price.replaceAll("[^0-9,.]", ""));
                            System.out.println( "link : "+ webSite.getUrl());
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


                           Long p = Long.parseLong(k);

                            Offer offer = new Offer(name.substring(0, Math.min(name.length(), 100)),webSite.getUrl(),p,offerStatus,category);
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
            Offer   existingOffer = offerRepositories.findTopByOfferCategoryAndOfferDescriptionAndOfferLink(offer.getOfferCategory(), offer.getOfferDescription(),offer.getOfferLink());
           // List<Offer>  exo = offerRepositories.findByOfferCategoryAndOfferDescriptionAndOfferLink(offer.getOfferCategory(), offer.getOfferDescription(),offer.getOfferLink());

           if(existingOffer==null){

              //offerRepositories.save(offer);
               offerList.add(offer);
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
}
