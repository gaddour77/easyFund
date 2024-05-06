package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Report;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.ReportRepository;
import tn.esprit.easyfund.repositories.IUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private static final int BAN_THRESHOLD = 5;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private IUserRepository userRepository;

    public void reportUser(Long reportedUserId, String subject) {
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + reportedUserId));

        Report report = new Report();
        report.setReportedUser(reportedUser);
        report.setSubject(subject);
        report.setReportTime(LocalDateTime.now());
        reportRepository.save(report);

        long reportCount = reportRepository.countByReportedUserUserId(reportedUserId);
        if (reportCount >= BAN_THRESHOLD) {
            reportedUser.setBanned(true);
            userRepository.save(reportedUser);
        }
    }

    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        if (user.isBanned()) {
            user.setBanned(false);
            userRepository.save(user);
        }
    }

    public List<User> findBannedUsers() {
        return userRepository.findByIsBanned(true);
    }

    // Additional methods as needed for your application
}

