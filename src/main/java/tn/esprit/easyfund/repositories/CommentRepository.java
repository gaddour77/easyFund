package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.easyfund.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
