package tn.esprit.easyfund.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="Adress")
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adressId;
    @NonNull
    private String houseNumber;
    @NonNull
    private String street;
    @NonNull
    private String postCode;
    @NonNull
    private String city;
    @NonNull
    private String country;
    @NonNull
    private String proofOfAdress;
    @OneToOne
    private Profile profile ;

}