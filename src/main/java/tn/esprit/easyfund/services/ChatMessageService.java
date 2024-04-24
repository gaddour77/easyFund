package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import tn.esprit.easyfund.entities.ChatMessage;
import tn.esprit.easyfund.repositories.ChatMessageRepository;

import java.util.List;
@AllArgsConstructor

public class ChatMessageService {
    private  ChatMessageRepository chatMessageRepository;
    public List<ChatMessage> messages(Long id){
        List<ChatMessage> messages = chatMessageRepository.findByChatIdOrderByDateAsc(id);
        return messages;
    }
}
