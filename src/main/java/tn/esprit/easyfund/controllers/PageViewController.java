package tn.esprit.easyfund.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.easyfund.entities.PageView;
import tn.esprit.easyfund.services.PageViewService;

@RestController
@RequestMapping("/api/page-views")
@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app

public class PageViewController {

    @Autowired
    private PageViewService pageViewService;

    @PostMapping
    public ResponseEntity<PageView> createPageView(@RequestBody PageViewRequest request) {
        PageView pageView = pageViewService.createPageView(request.getPageUrl());
        return ResponseEntity.ok(pageView);
    }

    static class PageViewRequest {
        private String pageUrl;

        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }
    }
}

