package tn.esprit.easyfund.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", referencedColumnName = "userId")
    private User reportedUser;

    private LocalDateTime reportTime = LocalDateTime.now();

    // Constructors, getters, and setters
}
