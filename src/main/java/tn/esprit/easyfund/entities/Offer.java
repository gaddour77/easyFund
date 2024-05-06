package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long offreId;
   // @NonNull
    private String offerDescription;
   // @NonNull
    private String offerLink;
   // @NonNull
    private float offerPrice;
  //  @NonNull
    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "offer")
    private Set<FinancingRequest> financingRequests;


}
