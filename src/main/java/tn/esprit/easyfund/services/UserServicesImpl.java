package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.List;
@Service
public class UserServicesImpl implements IUserServices {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProfileServices profileService;

    @Override
    public User registerUser(User user) {
        // Perform user registration logic

        // Save the user to the database
        User newUser = userRepository.save(user);

        // Create profile for the registered user
        profileService.createProfileForUser(newUser);

        return newUser;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {
        // Perform user update logic

        // Retrieve the user from the database
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            // Update user fields based on updatedUser
            // ...

            // Save the updated user to the database
            return userRepository.save(existingUser);
        } else {
            // Handle user not found
            return null;
        }
    }

    @Override
    public User banUser(Long userId) {
        // Perform user ban logic

        // Retrieve the user from the database
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            // Set user status to BANNED
            // existingUser.setUserStatus(UserStatus.BANNED);

            // Save the updated user to the database
            return userRepository.save(existingUser);
        } else {
            // Handle user not found
            return null;
        }
    }

    @Override
    public User showUserDetails(Long userId) {
        // Retrieve the user from the database
        return userRepository.findById(userId).orElse(null);
    }
}

