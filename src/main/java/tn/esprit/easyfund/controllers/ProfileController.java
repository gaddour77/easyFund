package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.Profile;
import tn.esprit.easyfund.services.IProfileServices;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private IProfileServices profileService;

    // Autowired IUserService...

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(@PathVariable Long userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Profile> createProfileForUser(@PathVariable Long userId) {
        Profile createdProfile = profileService.createProfileForUser(userId);

        if (createdProfile != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } else {
            // Handle creation failure (e.g., user not found)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long userId, @RequestBody Profile updatedProfile) {
        Profile updatedProfileData = profileService.updateProfile(userId, updatedProfile);

        if (updatedProfileData != null) {
            return ResponseEntity.ok(updatedProfileData);
        } else {
            // Handle user or profile not found
            return ResponseEntity.notFound().build();
        }
    }
}
