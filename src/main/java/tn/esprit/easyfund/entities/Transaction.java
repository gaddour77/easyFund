package tn.esprit.easyfund.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Trans_Id")
    private Long trans_id;
    @Column(name="Amount")
    private float amount;
    @Column(name="Acc_from")
    private String acc_from;
    @Column(name="Acc_to")
    private String acc_to;
    @Column(name="Ref_number")
    private String ref_number;
    @Column(name="Currency")
    private String currency;
    @Column(name="Trans_date")
    private LocalDate  trans_date;
    @Column(name="Trans_fee")
    private int  trans_fee;

    @Enumerated(EnumType.STRING)
    private TransactionType transType;
    @Enumerated(EnumType.STRING)
    private Status stat;

    @ManyToOne
    BankAccount bankAccount;
}
