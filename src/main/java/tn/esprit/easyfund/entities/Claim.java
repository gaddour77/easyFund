package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="Claim")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long claimId;
    @NonNull
    private String description;
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    @Enumerated(EnumType.STRING)

    private ClaimStatus claimStatus;
    @ManyToOne
    private User user;
    @NonNull
    private LocalDate dateOfBirth;
}