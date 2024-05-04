package tn.esprit.easyfund.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parametre {

    private String description;
    private float priceMin;
    private float priceMax;
    private float coff;
}
