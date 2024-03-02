package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.OfferCategory;
import tn.esprit.easyfund.entities.OfferStatus;
import tn.esprit.easyfund.entities.WebSite;
import tn.esprit.easyfund.repositories.IOfferRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OfferServicesImpl implements IOfferServices{
 private IOfferRepositories offerRepositories;

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
    public List<Offer> addScrap(){
        List<Offer> scrapOffers = new ArrayList<>();
        //partie webscrap
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
                if(con.response().statusCode()==200){
                    System.out.println(webSite.getUrl() + "trouvé");
                }
                Elements elements = doc.select(webSite.getBaliseName());
                if(!elements.isEmpty()){
                    for (Element row : elements){
                        String  name = row.select(webSite.getBaliseDescription()).text();
                        String  price =row.select(webSite.getBalisePrice()).text();

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
                            String k = price.replaceAll("[^0-9]","");
                           Long p = Long.parseLong(k);

                            Offer offer = new Offer(name.substring(0, Math.min(name.length(), 50)),webSite.getUrl(),p,offerStatus,category);
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
        for(Offer offer : scrapOffers){

            Offer   existingOffer = offerRepositories.findByOfferCategoryAndOfferDescriptionAndOfferLink(offer.getOfferCategory(), offer.getOfferDescription(),offer.getOfferLink());
           if(existingOffer==null){

              //offerRepositories.save(offer);
               offerList.add(offer);
           }

        }
               return offerRepositories.saveAll(offerList);
    }

}
