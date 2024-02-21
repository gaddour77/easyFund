package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MicroCredit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long microCreditId;
    private Date startDate;
    private Date dueDate;
    private float creditAmmount;
    private float creditRemaining;
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
    @Enumerated(EnumType.STRING)
    private CreditType creditType;
    @OneToOne
    private Account account;


}
