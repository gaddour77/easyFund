package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    private float balance;
    private Date creationDate;
    private Date updateDate;
    private Date lastLogin;
    private float score;
    private boolean isActive;
    private int accounRref;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "account")
    private Set<MicroCredit> microCredits;
    @OneToOne(mappedBy = "account")
    private MicroCredit microCredit;


}
