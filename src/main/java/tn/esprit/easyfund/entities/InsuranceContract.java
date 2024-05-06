
package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InsuranceContract  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @Enumerated(EnumType.STRING)
    private InsuranceContractStatus status;
    
    private double paidAmount;

    private boolean isRenewable;

    @ManyToOne
    @JoinColumn(name = "insurance_id")
    @JsonIgnore
    Insurance insurance;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    @JsonIgnore
    private User agent;


    @Transient
    private Long agentId;

    // Additional field to hold the insurance ID from JSON payload
    @Transient
    private Long insuranceId;
    
    // Getters and setters for insuranceId field
    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @Transient
    private Long userId;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}