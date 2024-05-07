package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    private String subject; // New field to store the report's subject

    private LocalDateTime reportTime = LocalDateTime.now();

    // Constructors, getters, and setters
}
