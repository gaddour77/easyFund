package tn.esprit.easyfund.repositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.ProjectRating;

@Repository
public interface ProjectRatingRepository extends JpaRepository<ProjectRating, Integer>
{
}
