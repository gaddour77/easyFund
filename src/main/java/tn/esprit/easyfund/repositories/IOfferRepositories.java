package tn.esprit.easyfund.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.OfferCategory;

import tn.esprit.easyfund.entities.OfferStatus;

import java.util.List;

public interface IOfferRepositories extends JpaRepository<Offer,Long> {
    @Query("SELECT fr FROM Offer fr WHERE fr.offerCategory = :offreC and fr.offerDescription =:offreD and fr.offerLink =:offerL")
    Offer findTopByOfferCategoryAndOfferDescriptionAndOfferLink(@Param("offreC") OfferCategory offreC, @Param("offreD") String offreD, @Param("offerL") String offerL);

    @Query("SELECT fr FROM Offer fr WHERE fr.offerStatus = :offres")
    List<Offer> finbByOfferStatus(@Param("offres") OfferStatus offerStatus);
}



