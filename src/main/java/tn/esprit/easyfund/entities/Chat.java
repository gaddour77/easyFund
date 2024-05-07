package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    private String name;

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST} ,mappedBy = "chat")
    private Set<ChatMessage> chatMessages;
    public Chat(String name){
        this.name=name;
    }
}
