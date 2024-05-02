package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Profile")
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
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