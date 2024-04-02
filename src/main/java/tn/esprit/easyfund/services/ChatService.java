package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Chat;
import tn.esprit.easyfund.repositories.IChatRepositories;

@AllArgsConstructor
@Service
public class ChatService {
    private IChatRepositories chatRepositories;
    public Chat findbyName(String name){
        return chatRepositories.findByName(name);
    }
}
