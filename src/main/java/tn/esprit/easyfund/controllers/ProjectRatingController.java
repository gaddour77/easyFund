package tn.esprit.easyfund.controllers;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.ProjectRating;
import tn.esprit.easyfund.services.ProjectRating.ProjectRatingService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ProjectRatingRating")
public class ProjectRatingController
{
    @Autowired
    ProjectRatingService ProjectRatingService;
    @PostMapping("/add-ProjectRating")
    @ResponseBody
    public ProjectRating addProjectRating(@RequestBody ProjectRating b) {

        return ProjectRatingService.add(b);
    }
    @GetMapping("/get_all_ProjectRatings")
    public List<ProjectRating> findAll() {
        return ProjectRatingService.findAll();
    }

    @PutMapping("/updateProjectRating/{id}")
    public ProjectRating update(@RequestBody ProjectRating ProjectRating ,@PathVariable("id") Integer id) {
        return ProjectRatingService.update(id,ProjectRating);
    }

    @DeleteMapping("/deleteProjectRating/{ProjectRatingId}")
    public void delete(@PathVariable("ProjectRatingId") Integer ProjectRatingId) {
        ProjectRatingService.delete(ProjectRatingId);
    }
}
