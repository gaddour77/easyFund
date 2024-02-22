package tn.esprit.easyfund.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    private String nom;
    @NonNull
    private String prenom;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private Long cin;

    private LocalDate dateOfBirth;

    private Float salary;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne
    private Account account;
    @OneToMany (cascade = CascadeType.ALL ,mappedBy = "user")
    private Set<FinancingRequest> financingRequests;


}
