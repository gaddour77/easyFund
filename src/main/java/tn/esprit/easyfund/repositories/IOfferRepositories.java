package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Offer;

public interface IOfferRepositories extends JpaRepository<Offer,Long> {

}
