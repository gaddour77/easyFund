package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Profile;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IProfileRepository;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.util.Optional;

@Service
public class ProfileServicesImpl implements IProfileServices {

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Profile getProfileByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return profileRepository.findByUser(user.get()).orElse(null);
        } else {
            // Handle user not found
            return null;
        }
    }

    @Override
    public Profile createProfileForUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Profile profile = new Profile();
            profile.setUser(user.get());
            return profileRepository.save(profile);
        } else {
            // Handle user not found
            return null;
        }
    }

    @Override
    public Profile updateProfile(Long userId, Profile updatedProfile) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Profile existingProfile = profileRepository.findByUser(user.get()).orElse(null);

            if (existingProfile != null) {
                // Update fields that are allowed to be updated
                existingProfile.setImage(updatedProfile.getImage());
                existingProfile.setName(updatedProfile.getName());
                existingProfile.setLastName(updatedProfile.getLastName());
                existingProfile.setSituation(updatedProfile.getSituation());
                existingProfile.setProfession(updatedProfile.getProfession());
                // Update other fields as needed

                return profileRepository.save(existingProfile);
            } else {
                // Handle profile not found
                return null;
            }
        } else {
            // Handle user not found
            return null;
        }
    }
}

