package tn.esprit.easyfund.entities;

import jakarta.persistence.*;

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@Column(unique = true)
    private String name;*/

    // Constructors, getters, setters...
}
