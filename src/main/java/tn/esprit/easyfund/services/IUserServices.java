package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.User;

import java.util.List;

public interface IUserServices {
    User registerUser(User user);
    User updateUser(Long userId, User updatedUser);
    User banUser(Long userId);
    User showUserDetails(Long userId);
}
