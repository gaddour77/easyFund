package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;
import tn.esprit.easyfund.entities.Offer;

public interface IMessageRepository extends JpaRepository<ChatMessage,Long> {

}
