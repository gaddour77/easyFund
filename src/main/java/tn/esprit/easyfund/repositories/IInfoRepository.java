package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.ElementInfo;

public interface IInfoRepository extends JpaRepository<ElementInfo,Long> {
}
