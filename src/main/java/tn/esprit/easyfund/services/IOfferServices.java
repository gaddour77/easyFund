package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Offer;

import java.util.List;

public interface IOfferServices {
 public Offer addOffer(Offer offer);
 public Offer findById(long id);
public List<Offer> addOffers( List<Offer> listOffers);
public Offer updateOffer(Offer offer);
public List<Offer> findAll();
public String delete( long id);

}
