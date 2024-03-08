package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.ChangePasswordRequest;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.security.Principal;

@Service
public class UserServicesImpl implements IUserServices {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private IProfileServices profileService;

    @Override
    public User registerUser(User user) {

        User newUser = userRepository.save(user);

        profileService.createProfileForUser(newUser);

        return newUser;
    }

    @Override
    public User updateUser(Long userId, User updatedUser) {

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
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            // Set isBanned to true
            user.setBanned(true);
            userRepository.save(user);

            // Return the banned user
            return user;
        } catch (Exception e) {
            // Handle exceptions (log or perform other actions)
            e.printStackTrace();
            // You may throw a custom exception or handle it based on your needs
            return null; // Return null or throw a specific exception based on your error-handling strategy
        }
    }

    @Override
    public User showUserDetails(Long userId) {
        // Retrieve the user from the database
        return userRepository.findById(userId).orElse(null);
    }
    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

}

