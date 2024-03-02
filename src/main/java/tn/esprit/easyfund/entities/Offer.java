package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @NonNull
    private String offerDescription;
    @NonNull
    private String offerLink;
    @NonNull
    private float offerPrice;
    @NonNull
    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;
    @Enumerated(EnumType.STRING)
    private  OfferCategory offerCategory;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "offer")
    private Set<FinancingRequest> financingRequests;
    public Offer(@NonNull String offerDescription, @NonNull String offerLink, @NonNull float offerPrice, @NonNull OfferStatus offerStatus, OfferCategory offerCategory) {
        this.offerDescription = offerDescription;
        this.offerLink = offerLink;
        this.offerPrice = offerPrice;
        this.offerStatus = offerStatus;
        this.offerCategory = offerCategory;

    }


}
