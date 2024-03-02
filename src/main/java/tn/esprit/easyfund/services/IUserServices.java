package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.ChangePasswordRequest;
import tn.esprit.easyfund.entities.User;

import java.security.Principal;
import java.util.List;

public interface IUserServices {
    User registerUser(User user);
    User updateUser(Long userId, User updatedUser);
    User banUser(Long userId);
    User showUserDetails(Long userId);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
