package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.repositories.IChatRepositories;


import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class ChatService {
    private IChatRepositories chatRepositories;
    public Chat findbyName(String name){
        return chatRepositories.findByName(name);
    }

    public List<Chat>findChats(long idu){
        List<Chat> chats = new ArrayList<>();
        chats = chatRepositories.findChatsByUserId(idu);
        return chats;
    }

}
