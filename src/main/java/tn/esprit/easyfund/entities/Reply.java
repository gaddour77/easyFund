package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Reply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "claim_id")
    @JsonIgnore
    private Claim claim;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    // Optionally link to the user who made the reply
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User responder;
}

