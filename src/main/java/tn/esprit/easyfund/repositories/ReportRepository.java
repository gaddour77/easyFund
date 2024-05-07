package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.easyfund.entities.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Count reports by the reported user's ID
    long countByReportedUserUserId(Long userId);

    // Example custom query to find reports by subject (if needed)
    List<Report> findBySubjectContaining(String subject);
}
