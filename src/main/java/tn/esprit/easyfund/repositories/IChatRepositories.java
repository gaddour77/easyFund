package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.Offer;
import tn.esprit.easyfund.entities.OfferCategory;

public interface IChatRepositories extends JpaRepository<Chat,Long> {
    @Query("SELECT fr FROM Chat fr WHERE fr.name = :offreC ")
    Chat findByName(@Param("offreC") String name );

}
