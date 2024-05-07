package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.dto.Parametre;

public interface IparametreRepository extends JpaRepository<Parametre,Long> {
}
