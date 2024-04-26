package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Profile;
import tn.esprit.easyfund.entities.User;

public interface IProfileServices {
    Profile getProfileByUserId(Long userId);
    Profile createProfileForUser(User user);
    Profile updateProfile(Long userId, Profile updatedProfile);

}
