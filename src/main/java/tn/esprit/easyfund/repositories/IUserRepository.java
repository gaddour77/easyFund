package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.User;

public interface IUserRepository extends JpaRepository<User, Long> {
}