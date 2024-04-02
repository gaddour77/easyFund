package tn.esprit.easyfund.services;

import org.springframework.data.crossstore.ChangeSetPersister;
import tn.esprit.easyfund.entities.ChangePasswordRequest;
import tn.esprit.easyfund.entities.User;

import java.security.Principal;

public interface IUserServices {
    User registerUser(User user);
    User updateUser(Long userId, User updatedUser);
    User banUser(Long userId) throws ChangeSetPersister.NotFoundException;
    User showUserDetails(Long userId);
    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}
