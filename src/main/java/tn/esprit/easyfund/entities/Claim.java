package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Claim {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;
    private String title ; 

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus;



    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();
    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIgnore
    private User agent;
    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();
    public Claim(Long claimId, String description, ClaimType claimType, ClaimStatus claimStatus, User user, Date createdAt, String title) {
        this.claimId = claimId;
        this.description = description;
        this.claimType = claimType;
        this.claimStatus = claimStatus;
        this.user = user;
        this.createdAt = createdAt;
        this.title=title;   }
}
