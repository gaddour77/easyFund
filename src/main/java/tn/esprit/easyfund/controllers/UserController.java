package tn.esprit.easyfund.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.easyfund.entities.Comment;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.UserRepository;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/User")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/getall")
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
