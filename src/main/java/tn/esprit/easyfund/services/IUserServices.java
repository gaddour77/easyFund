package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.User;

import java.util.List;

public interface IUserServices {
    User getUserById(Long userId);
    List<User> getAllUsers();
    User saveUser(User user);
    User updateUser(Long userId, User updatedUser);
    void deleteUser(Long userId);
}
