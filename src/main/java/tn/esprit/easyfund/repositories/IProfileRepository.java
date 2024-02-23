package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Profile;
import tn.esprit.easyfund.entities.User;

import java.util.Optional;

public interface IProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);
}