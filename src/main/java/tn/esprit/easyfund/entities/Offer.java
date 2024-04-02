package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private static final long serialVersionUID = 1L;
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

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.PERSIST} ,mappedBy = "offer")
    private Set<FinancingRequest> financingRequests;
    public Offer(@NonNull String offerDescription, @NonNull String offerLink, @NonNull float offerPrice, @NonNull OfferStatus offerStatus, OfferCategory offerCategory) {
        this.offerDescription = offerDescription;
        this.offerLink = offerLink;
        this.offerPrice = offerPrice;
        this.offerStatus = offerStatus;
        this.offerCategory = offerCategory;

    }


}
