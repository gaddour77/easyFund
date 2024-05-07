package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.dto.ElementInfo;

public interface IInfoRepository extends JpaRepository<ElementInfo,Long> {
}
