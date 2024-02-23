package tn.esprit.easyfund.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Project implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectId;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Long amountRequired;
	private Long currentAmount;
	private ProjectStatus projectStatus;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	InvestementRequest investementRequest;

	@JsonIgnore
	@ManyToMany(mappedBy = "projects",cascade = CascadeType.ALL)
	List<InvestementRequest> investementRequests;

	@ManyToOne
	User user;
}
