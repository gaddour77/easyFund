package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Comment;
import tn.esprit.easyfund.entities.Project;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.ProjectRepository;
import tn.esprit.easyfund.repositories.UserRepository;
import tn.esprit.easyfund.services.CommentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Comment")
public class CommentController {
    @Autowired
    CommentService CommentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @PostMapping("/add-Comment/{iduser}/{idproject}")
    @ResponseBody
    public Comment addComment(@RequestBody Comment b,@PathVariable("iduser") Integer iduser,@PathVariable("idproject") Integer idproject) {
        User user = userRepository.findById(iduser).get();
        b.setUser(user);
        Project project = projectRepository.findById(idproject).get();
        b.setProject(project);
        return CommentService.add(b);
    }
    @GetMapping("/get_all_Comments")
    public List<Comment> findAll() {
        return CommentService.findAll();
    }

    @PutMapping("/updateComment/{id}")
    public Comment update(@RequestBody Comment Comment ,@PathVariable("id") Integer id) {
        return CommentService.update(id,Comment);
    }

    @DeleteMapping("/deleteComment/{CommentId}")
    public void delete(@PathVariable("CommentId") Integer CommentId) {
        CommentService.delete(CommentId);
    }
}

