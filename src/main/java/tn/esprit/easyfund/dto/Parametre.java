package tn.esprit.easyfund.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Parametre {
    @Id
    private Long idP;
    private String description;
    private float priceMin;
    private float priceMax;
    private float coff;

}
