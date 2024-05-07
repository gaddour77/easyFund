package tn.esprit.easyfund.controllers;

import org.springframework.http.ResponseEntity;
import tn.esprit.easyfund.entities.Reply;
import tn.esprit.easyfund.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
@CrossOrigin(origins = "http://localhost:4200") // Allow cross-origin requests from Angular app

public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping("/by-claim/{claimId}")
    public List<Reply> getRepliesByClaimId(@PathVariable Long claimId) {
        return replyService.getRepliesByClaimId(claimId);
    }

    @PostMapping("/save/{claimId}")
    public ResponseEntity<Reply> saveReply(@PathVariable Long claimId, @RequestBody Reply reply) {
        // Call the service method to save the reply
        Reply savedReply = replyService.saveReply(claimId, reply);
        return ResponseEntity.ok(savedReply);
    }
}

