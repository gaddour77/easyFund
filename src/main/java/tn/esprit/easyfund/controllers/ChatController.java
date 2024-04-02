package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;
import tn.esprit.easyfund.repositories.ChatMessageRepository;
import tn.esprit.easyfund.repositories.IChatRepositories;

import java.util.List;

@Controller
@AllArgsConstructor
public class ChatController {
    private final ChatMessageRepository messageRepository;
    private final IChatRepositories chatRepositories;
     @GetMapping("/chats")
     public List<Chat> chats(Long id){
         return null;
     }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ) {
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
