package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.services.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/reportUser")
    public ResponseEntity<?> reportUser(@RequestParam Long reportedUserId, @RequestParam String subject) {
        reportService.reportUser(reportedUserId, subject);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unbanUser")
    public ResponseEntity<?> unbanUser(@RequestParam Long userId) {
        reportService.unbanUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bannedUsers")
    public ResponseEntity<List<User>> findBannedUsers() {
        return ResponseEntity.ok(reportService.findBannedUsers());
    }
}

