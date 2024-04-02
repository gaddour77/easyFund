package tn.esprit.easyfund.controllers;



import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.entities.ChatMessage;

import tn.esprit.easyfund.repositories.IChatRepositories;
import tn.esprit.easyfund.repositories.IMessageRepository;

@RestController
@RequestMapping("/chatcontoller")
public class ChatController1 {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private IChatRepositories chatDAO;
    @Autowired
    private IMessageRepository messageDAO;

    @MessageMapping("/chat/{to}") //to = nome canale
    public void sendMessage(@DestinationVariable String to , ChatMessage message) {
        System.out.println("handling send message: " + message + " to: " + to);
        message.setMessageId(createAndOrGetChat(to));
        message.setStamp(generateTimeStamp());
        message = messageDAO.save(message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }
    @GetMapping("find/{id}")
    public List<Chat> allmessage(@PathVariable long id){

        return chatDAO.findAll();
    }
    /*

    @PostMapping("/getChats")
    public List<ChatEntity> getChats(@RequestBody String user){
        return chatDAO.findByPartecipant(user);
    }

    //returns an empty list if the chat doesn't exist
    @PostMapping("/getMessages")
    public List<MessageEntity> getMessages(@RequestBody String chat) {
        ChatEntity ce = chatDAO.findByName(chat);

        if(ce != null) {
            return messageDAO.findAllByChat(ce.getChat_id());
        }
        else{
            return new ArrayList<MessageEntity>();
        }
    }
*/
    //finds the chat whose name is the parameter, if it doesn't exist it gets created, the ID gets returned either way
    private Long createAndOrGetChat(String name) {

        Chat ce = chatDAO.findByName(name);


        if (ce != null) {
            return ce.getChatId();
        }
        else {
            Chat newChat = new Chat(name);
            return chatDAO.save(newChat).getChatId();
        }
    }

    private String generateTimeStamp() {
        Instant i = Instant.now();
        String date = i.toString();
        System.out.println("Source: " + i.toString());
        int endRange = date.indexOf('T');
        date = date.substring(0, endRange);
        date = date.replace('-', '/');
        System.out.println("Date extracted: " + date);
        String time = Integer.toString(i.atZone(ZoneOffset.UTC).getHour() + 1);
        time += ":";

        int minutes = i.atZone(ZoneOffset.UTC).getMinute();
        if (minutes > 9) {
            time += Integer.toString(minutes);
        } else {
            time += "0" + Integer.toString(minutes);
        }

        System.out.println("Time extracted: " + time);
        String timeStamp = date + "-" + time;
        return timeStamp;
    }
}
