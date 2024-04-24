package tn.esprit.easyfund.controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;
import tn.esprit.easyfund.repositories.ChatMessageRepository;
import tn.esprit.easyfund.repositories.IChatRepositories;
import tn.esprit.easyfund.services.ChatMessageService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api")
@AllArgsConstructor
public class ChatController {
    private SimpMessagingTemplate messagingTemplate;

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
         chatMessage.setDate(new Date());
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
    @PostMapping("/send-message")
    public void sendMessage1(@RequestBody ChatMessage message) {
        // Envoi du message WebSocket
        message.setDate(new Date());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/public", message);
    }
    @GetMapping("/chats/{id}")
    public List<Chat>chats(@PathVariable long id){
         List<Chat> chats = new ArrayList<>();
         chats = chatRepositories.findChatsByUserId(id);
        System.out.println(chats);
         return chats;
    }
    @GetMapping("/messages/{id}")
    public List<ChatMessage> messages(@PathVariable long id){
         List<ChatMessage> messages = messageRepository.findByChatIdOrderByDateAsc(id);

         return messages;
    }
}
