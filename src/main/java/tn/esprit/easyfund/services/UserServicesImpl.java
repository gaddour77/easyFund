package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.List;
@Service
public class UserServicesImpl implements IUserServices{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        // Add any additional business logic if needed
        return userRepository.save(user);
    }
    @Override
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            // Update fields that are allowed to be updated
            existingUser.setNom(updatedUser.getNom());
            existingUser.setPrenom(updatedUser.getPrenom());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setCin(updatedUser.getCin());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            existingUser.setSalary(updatedUser.getSalary());
            existingUser.setUserStatus(updatedUser.getUserStatus());
            existingUser.setRole(updatedUser.getRole());
            // Update other fields as needed

            return userRepository.save(existingUser);
        } else {
            // Handle user not found
            return null;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
