package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FinancingRequest  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long financingRequestId;
    @NonNull
    private LocalDate dateFinancingRequest;
    @NonNull
    private LocalDate finalDate;
    private String excel;
    @NonNull
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name ="offerId")
    private Offer offer;
    @ManyToOne
    private User user;




}
