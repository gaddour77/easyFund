package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.repositories.IOfferRepositories;
import tn.esprit.easyfund.services.IOfferServices;

@AllArgsConstructor
@Service
public class OfferServicesImpl implements IOfferServices {
    private IOfferRepositories offerRepositories;

    @Override
    public Offer addOffer(Offer offer) {
        return offerRepositories.save(offer);
    }
    public Offer findById(long id){
        return offerRepositories.findById(id).orElse(null);

    }
}
