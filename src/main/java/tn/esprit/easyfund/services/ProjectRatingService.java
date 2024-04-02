package tn.esprit.easyfund.services;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.ProjectRating;
import tn.esprit.easyfund.repositories.ProjectRatingRepository;
import tn.esprit.easyfund.services.ICRUDService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectRatingService implements ICRUDService<ProjectRating,Integer> {
    @Autowired
    ProjectRatingRepository projectRatingRepository ;
    @Override
    public List<ProjectRating> findAll() {
        return projectRatingRepository.findAll();
    }

    @Override
    public ProjectRating retrieveItem(Integer idItem) {
        return projectRatingRepository.findById(idItem).get();
    }

    @Override
    public ProjectRating add(ProjectRating class1) {
        return projectRatingRepository.save(class1);
    }

    @Override
    public void delete(Integer id) {
         projectRatingRepository.deleteById(id);
    }

    @Override
    public ProjectRating update(Integer id ,ProjectRating ProjectRating) {
        Optional<ProjectRating> existingProjectOptional = projectRatingRepository.findById(id);

        if (existingProjectOptional.isPresent()) {
            ProjectRating existingProject = existingProjectOptional.get();
            existingProject.setRating(ProjectRating.getRating());

            return projectRatingRepository.save(existingProject);
        } else {
            return null;
        }}
}
