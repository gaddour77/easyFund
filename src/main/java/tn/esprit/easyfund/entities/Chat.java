package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatId;
    private String name;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST} ,mappedBy = "chat")
    private Set<ChatMessage> chatMessages;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private Set<User> participants;


  
    public Chat(String name){
        this.name=name;
    }
}
