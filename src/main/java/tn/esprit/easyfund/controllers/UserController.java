package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.services.IUserServices;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app

public class UserController {

    @Autowired
    private IUserServices userService;


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
    @GetMapping("/banned")
    public ResponseEntity<List<UserDTO>> getBannedUsers() {
        List<User> bannedUsers = userService.findBannedUsers();
        List<UserDTO> userDTOs = bannedUsers.stream().map(user -> new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(), // Assuming getRole() returns an enum or similar object
                user.getCin(),
                user.getSalary()
        )).collect(Collectors.toList());
        return userDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAllNonBannedUsers();
        List<UserDTO> userDTOs = users.stream().map(user -> new UserDTO(
                user.getUserId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getCin(),
                user.getSalary()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    @PutMapping("/unban/{userId}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        boolean success = userService.unbanUser(userId);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    public class UserDTO {
        private Long userId;
        private String firstname;
        private String lastname;
        private String email;
        private String phoneNumber;
        private String role;
        private Long cin;
        private Float salary;


        // Default constructor
        public UserDTO() {
        }

        public Long getCin() {
            return cin;
        }

        public Float getSalary() {
            return salary;
        }

        public void setCin(Long cin) {
            this.cin = cin;
        }

        public void setSalary(Float salary) {
            this.salary = salary;
        }

        // All-args constructor
        public UserDTO(Long userId, String firstname, String lastname, String email, String phoneNumber, String role,Long cin,Float salary) {
            this.userId = userId;
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.role = role;
            this.cin = cin;
            this.salary=salary;
        }

        // Getters and setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}


