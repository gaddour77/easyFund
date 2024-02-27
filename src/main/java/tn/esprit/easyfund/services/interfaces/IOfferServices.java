package tn.esprit.easyfund.services.interfaces;

import tn.esprit.easyfund.entities.Offer;

public interface IOfferServices {
 public Offer addOffer(Offer offer);
 public Offer findById(long id);

}
