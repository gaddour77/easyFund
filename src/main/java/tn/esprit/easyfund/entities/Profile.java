package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String image;
    private String name;
    private String lastName;
    private String situation;
    private String profession;
    @OneToOne
    private Adress adress ;
    @OneToOne
    private User user;

}