package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.repositories.IOfferRepositories;

import java.util.List;

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
}
