package tn.esprit.easyfund.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="User")
public class User implements UserDetails , Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    @NonNull
    @Column(unique = true)
    @Email
    private String email;
    @NonNull
    private String password;

    private Long cin;
    private String phoneNumber;
    private boolean twoFactorAuthEnabled;
    private String twoFactorAuthSecret ;




    private LocalDate dateOfBirth;

    private Float salary;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne
    private Account account;

  
    @OneToMany(mappedBy = "agent")

    @JsonIgnore
    @OneToMany(mappedBy = "agent")
    private List<Claim> assignedClaims;
    @JsonIgnore
    @OneToMany(mappedBy = "user")

    private List<Claim> submittedClaims;
    @JsonIgnore
    @OneToMany(mappedBy = "user")

    private List<Token> tokens;


    @JsonIgnore

  @OneToMany (cascade = CascadeType.ALL ,mappedBy = "user")
    private Set<FinancingRequest> financingRequests;
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;
    private boolean isBanned ;
    private String validationCode;
    private LocalDateTime validationCodeTimestamp;


    @Enumerated(EnumType.STRING)
    private ValidationMethod validationMethod;

   /* @ManyToMany
    @JoinTable(
            name = "user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private Set<Interest> interests = new HashSet<>();*/


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User orElse(Object o) {
        return null ;
    }
}
