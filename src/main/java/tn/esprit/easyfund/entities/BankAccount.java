package tn.esprit.easyfund.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Entity
@Getter
@Setter

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
    private String count_ref;
    @Column(name="Score")
    private int score=0;
    @Column(name="Is_Active")
    private boolean is_active = true;;
    @Column(name="Create_date")
    private LocalDate  create_date = LocalDate.now(); // Default to current date;

    @Column(name="Update_date")
    private LocalDate  update_date = create_date; // Default to creation date;

    @Enumerated(EnumType.STRING)
    private AccountType typeAcc;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "bankAccount")
    private Set<Transaction> transactions;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore// This is the foreign key column in the BankAccount table
    private User user;
    @Override
    public String toString() {
        return "BankAccount{" +
                "account_id=" + account_id +
                ", balance=" + balance +
                ", count_ref=" + count_ref +
                ", score=" + score +
                ", is_active=" + is_active +
                ", create_date=" + create_date +
                ", update_date=" + update_date +
                ", typeAcc=" + typeAcc +
                '}';
    }


}