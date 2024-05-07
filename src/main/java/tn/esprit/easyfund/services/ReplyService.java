package tn.esprit.easyfund.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.easyfund.entities.Claim;
import tn.esprit.easyfund.entities.Reply;
import tn.esprit.easyfund.entities.User;
import tn.esprit.easyfund.repositories.IClaimRepository;
import tn.esprit.easyfund.repositories.IUserRepository;
import tn.esprit.easyfund.repositories.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IClaimRepository claimRepository ;

    public List<Reply> getRepliesByClaimId(Long claimId) {
        return replyRepository.findByClaim_ClaimId(claimId);
    }

    public Reply saveReply(Long claimId , Reply reply) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Find the user based on the username (assuming username is the email)
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Set the user for the claim
        reply.setResponder(user);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        // Set the claim for the reply
        reply.setClaim(claim);

        return replyRepository.save(reply);
    }
}

