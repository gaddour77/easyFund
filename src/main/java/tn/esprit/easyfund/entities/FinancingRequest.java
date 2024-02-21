package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FinancingRequest  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long financingRequestId;
    @NonNull
    private Date dateFinancingRequest;
    @NonNull
    private Date finalDate;
    @NonNull
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @ManyToOne
    private Offer offer;
    @ManyToOne
    private User user;




}
