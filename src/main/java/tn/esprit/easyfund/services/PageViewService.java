package tn.esprit.easyfund.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.PageView;
import tn.esprit.easyfund.repositories.PageViewRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageViewService {

    @Autowired
    private PageViewRepository pageViewRepository;

    public PageView createPageView(String url) {
        PageView pageView = new PageView();
        pageView.setPageUrl(url);
        pageView.setTimestamp(LocalDateTime.now());
        return pageViewRepository.save(pageView);
    }
    public long getPageViewsForToday() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return pageViewRepository.countByDateRange(startOfDay, endOfDay);
    }

    public long getPageViewsForYesterday() {
        LocalDateTime startOfYesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime endOfYesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX);
        return pageViewRepository.countByDateRange(startOfYesterday, endOfYesterday);
    }

    public double calculatePercentageChange(long todayViews, long yesterdayViews) {
        if (yesterdayViews == 0) {
            return todayViews > 0 ? 100.0 : 0.0;  // Handle division by zero
        }
        return ((double) (todayViews - yesterdayViews) / yesterdayViews) * 100;
    }
    public Map<LocalDate, Long> getViewsForLast7Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // Last 7 days including today

        List<PageView> pageViews = pageViewRepository.findByTimestampBetween(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));

        return pageViews.stream()
                .collect(Collectors.groupingBy(pv -> pv.getTimestamp().toLocalDate(), Collectors.counting()));
    }
    public Map<String, Long> getTopUrls() {
        List<Object[]> results = pageViewRepository.findTopUrls();

        return results.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> (Long) obj[1]
                ));
    }


}
