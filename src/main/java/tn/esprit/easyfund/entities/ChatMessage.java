package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="chatmessage")
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    private MessageType type;
    private String content;
    private String sender;
    private String stamp;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name ="chatId")
    private Chat chat;

}
