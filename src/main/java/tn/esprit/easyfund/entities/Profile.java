package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "userId")
    private User user;

}