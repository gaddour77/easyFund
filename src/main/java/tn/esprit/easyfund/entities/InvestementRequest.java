package tn.esprit.easyfund.entities;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvestementRequest implements Serializable
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer investementId;
	private Long amountInvested;
	private StatusInv statusInv;


	@ManyToMany
	List<Project> projects;


	@OneToMany(mappedBy = "investementRequest")
	List<Project> projectList;

	@ManyToOne
	User user;

}
