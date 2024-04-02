package tn.esprit.easyfund.services;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.InvestementRequest;
import tn.esprit.easyfund.entities.Project;
import tn.esprit.easyfund.repositories.ProjectRepository;
import tn.esprit.easyfund.services.ICRUDService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService implements ICRUDService<Project,Integer>
{
    @Autowired
    ProjectRepository projectRepository ;
    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project retrieveItem(Integer idItem) {
        return projectRepository.findById(idItem).get();
    }

    @Override
    public Project add(Project class1) {
        return projectRepository.save(class1);
    }

    @Override
    public void delete(Integer id) {
        projectRepository.deleteById(id);

    }

    @Override
    public Project update(Integer id,Project Project) {
        Optional<Project> existingProjectOptional = projectRepository.findById(id);

        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();
            existingProject.setName(Project.getName());

            return projectRepository.save(existingProject);
        } else {
            return null;
        }
    }
}
