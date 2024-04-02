package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.apache.http.annotation.Contract;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Insurance  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private InsuranceType type;

    @Enumerated(EnumType.STRING)
    private Beneficiary beneficiary;
    
    private double coverageAmount;


    @OneToMany(mappedBy = "insurance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InsuranceContract> contracts;
}