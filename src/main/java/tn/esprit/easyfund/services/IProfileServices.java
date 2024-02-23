package tn.esprit.easyfund.services;

import tn.esprit.easyfund.entities.Profile;

public interface IProfileServices {
    Profile getProfileByUserId(Long userId);
    Profile createProfileForUser(Long userId);
    Profile updateProfile(Long userId, Profile updatedProfile);

}
