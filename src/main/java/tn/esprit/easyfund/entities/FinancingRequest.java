package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

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
