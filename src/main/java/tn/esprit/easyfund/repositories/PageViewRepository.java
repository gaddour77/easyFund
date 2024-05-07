package tn.esprit.easyfund.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.easyfund.entities.PageView;

import java.time.LocalDateTime;
import java.util.List;

public interface PageViewRepository extends JpaRepository<PageView, Long> {
    List<PageView> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT count(p) FROM PageView p WHERE p.timestamp BETWEEN ?1 AND ?2")
    long countByDateRange(LocalDateTime start, LocalDateTime end);
    @Query("SELECT pv.pageUrl, COUNT(pv) " +
            "FROM PageView pv " +
            "GROUP BY pv.pageUrl " +
            "ORDER BY COUNT(pv) DESC")
    List<Object[]> findTopUrls();
}
