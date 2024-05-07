package tn.esprit.easyfund.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<User> findByIsBannedTrue();
    List<User> findByIsBannedFalse();

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isBanned = false WHERE u.userId = :userId")
    int unbanUserById(@Param("userId") Long userId);
}