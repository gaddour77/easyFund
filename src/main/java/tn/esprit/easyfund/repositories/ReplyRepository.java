package tn.esprit.easyfund.repositories;

import tn.esprit.easyfund.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByClaim_ClaimId(Long claimId);
}
