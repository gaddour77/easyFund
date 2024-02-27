package tn.esprit.easyfund.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="BankAccount")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="AccountId")
    private Long account_id;

    @Column(name="Balance")
    private float balance;
    @Column(name="Count_ref")
    private int count_ref;
    @Column(name="Score")
    private int score;
    @Column(name="Is_Active")
    private boolean is_active;
    @Column(name="Create_date")
    private LocalDate  create_date;

    @Column(name="Update_date")
    private LocalDate  update_date;

    @Enumerated(EnumType.STRING)
    private AccountType typeAcc;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bankAccount")
    private Set<Transaction> transactions;
}
