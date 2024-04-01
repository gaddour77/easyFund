package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "MICRO_CREDITS")
@Entity
public  class MicroCredit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long microCreditId;

    private LocalDate startDate;
    private LocalDate dueDate;

    @Min(value = 2, message = "Period must be equal or greater than 2")
    @Max(value = 48, message = "Period must be equal or less than 48")
    private Integer period;

    @Enumerated(EnumType.STRING)
    private TypePeriod typePeriod;

    @Min(value = 500, message = "Amount must be equal or greater than 100 TND")
    @Max(value = 40000, message = "Amount must be equal or greater than 100 TND")
    @NotNull(message = "Credit Amount cannot be empty")
    private float creditAmmount;

    @NotNull(message = "Amount Remaining cannot be empty")
    private float creditRemaining;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Credit Status cannot be empty")
    private CreditStatus creditStatus;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Credit Type cannot be empty")
    private CreditType creditType;

//    @NotNull(message = "Payed Amount cannot be empty")
    private Double payedAmount;

    /* @OneToOne()
    @NotNull(message = "Guarantor Account cannot be empty")
    private Account guarantorAccount; */

    //@NotNull(message = "Guarantor Prof cannot be empty")
    private byte[] guarantorFile;

    @NotNull(message = "Interest Rate cannot be empty")
    // @Min(value = ,message = "Interest Rate can't be lower than 7")
    // @Max(value = ,message = "Interest Rate can't be lower than 7")
    private float interestRate;

    @ManyToOne
    private Account accountFK;

}
