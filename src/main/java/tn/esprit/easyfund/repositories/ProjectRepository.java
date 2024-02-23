package tn.esprit.easyfund.repositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.easyfund.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>
{
}
