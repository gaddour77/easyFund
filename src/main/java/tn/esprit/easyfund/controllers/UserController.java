package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.services.IUserServices;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserServices userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/ban/{userId}")
    public ResponseEntity<User> banUser(@PathVariable Long userId) throws ChangeSetPersister.NotFoundException {
        User bannedUser = userService.banUser(userId);
        if (bannedUser != null) {
            return ResponseEntity.ok(bannedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<User> showUserDetails(@PathVariable Long userId) {
        User user = userService.showUserDetails(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

