package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("SELECT m FROM ChatMessage m WHERE m.chat.chatId = :chatId ORDER BY m.date ASC")
    List<ChatMessage> findByChatIdOrderByDateAsc(Long chatId);

}
