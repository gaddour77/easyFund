package tn.esprit.easyfund.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.easyfund.entities.Role;
import tn.esprit.easyfund.entities.User;

public interface IUserRepository extends JpaRepository<User, Long>{
}
