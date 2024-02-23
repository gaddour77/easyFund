package tn.esprit.easyfund.controllers;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Project;
import tn.esprit.easyfund.services.Project.ProjectService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Project")
public class ProjectController
{
    @Autowired
    ProjectService ProjectService;
    @PostMapping("/add-Project")
    @ResponseBody
    public Project addProject(@RequestBody Project b) {

        return ProjectService.add(b);
    }
    @GetMapping("/get_all_Projects")
    public List<Project> findAll() {
        return ProjectService.findAll();
    }

    @PutMapping("/updateProject/{id}")
    public Project update(@RequestBody Project Project ,@PathVariable("id") Integer id) {
        return ProjectService.update(id,Project);
    }

    @DeleteMapping("/deleteProject/{ProjectId}")
    public void delete(@PathVariable("ProjectId") Integer ProjectId) {
        ProjectService.delete(ProjectId);
    }
}
