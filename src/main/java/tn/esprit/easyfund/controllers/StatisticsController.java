package tn.esprit.easyfund.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.easyfund.services.PageViewService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app

public class StatisticsController {

    @Autowired
    private PageViewService pageViewService;

    @GetMapping("/today-stats")
    public ResponseEntity<Object> getTodayStatistics() {
        long todayViews = pageViewService.getPageViewsForToday();
        long yesterdayViews = pageViewService.getPageViewsForYesterday();
        double percentageChange = pageViewService.calculatePercentageChange(todayViews, yesterdayViews);

        // Create a JSON object with the statistics data
        Map<String, Object> response = new HashMap<>();
        response.put("todayViews", todayViews);
        response.put("percentageChange", percentageChange);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/last-7-days")
    public ResponseEntity<Map<LocalDate, Long>> getViewsForLast7Days() {
        Map<LocalDate, Long> viewsForLast7Days = pageViewService.getViewsForLast7Days();
        return ResponseEntity.ok(viewsForLast7Days);
    }
    @GetMapping("/top-urls")
    public ResponseEntity<Map<String, Long>> getTopUrls() {
        Map<String, Long> topUrls = pageViewService.getTopUrls();

        // Handle null values in the response
        if (topUrls == null) {
            // Replace null with an empty map or handle it based on your requirement
            topUrls = new HashMap<>();
        }

        return ResponseEntity.ok(topUrls);
    }
}
