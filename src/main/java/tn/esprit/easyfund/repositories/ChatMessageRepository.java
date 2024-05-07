package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
