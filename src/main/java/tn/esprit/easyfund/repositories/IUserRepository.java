package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.easyfund.entities.Role;
import tn.esprit.easyfund.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //List<User> findByInterestsContains(String interest);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.assignedClaims c WHERE u.role = 'AGENT' AND c.claimStatus = 'OPEN'")
    List<User> findAgentsWithOpenClaims();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.assignedClaims c WHERE u.role = 'AGENT' AND c.claimStatus = 'OPEN' " +
            " ORDER BY COUNT(c) ASC LIMIT 1")
    User findAgentWithLeastOpenClaims();
    Optional<User> findTopByRoleOrderByUserIdAsc(Role role);

    List<User> findByIsBanned(boolean isBanned);

}