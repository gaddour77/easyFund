package tn.esprit.easyfund.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ElementInfo {
    @Id
    private Long id;
    private float time;
}
