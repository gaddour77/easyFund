package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Table(name = "MICRO_CREDIT")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MicroCredit implements Serializable {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long microCreditId;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    @NotNull(message = "Start Date cannot be empty")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    @NotNull(message = "Due Date cannot be empty")
    private Date dueDate;

    @Min(value = 2, message = "Period must be equal or greater than 2")
    @Max(value = 84, message = "Period must be equal or less than 48")
    @NotNull(message = "Period cannot be empty")
    private Integer period;

    //@Min(value = 100, message = "Amount must be equal or greater than 100 DTN")
    //@Max(value = 100, message = "Amount must be equal or greater than 100 DTN")
    @NotNull(message = "Credit Amount cannot be empty")
    private float creditAmmount;

    @NotNull(message = "Amount Remaining cannot be empty")
    private float creditRemaining;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Credit Status cannot be empty")
    private CreditStatus creditStatus;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    @OneToOne()
    @NotNull(message = "Guarantor Account cannot be empty")
    private Account guarantorAccount;

    @NotNull(message = "Guarantor Prof cannot be empty")
    private byte[] guarantorFile;

    @NotNull(message = "Interest Rate cannot be empty")
    // @Min(value = ,message = "Interest Rate can't be lower than 7")
    // @Max(value = ,message = "Interest Rate can't be lower than 7")
    private float interestRate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Account accountFK;
}
