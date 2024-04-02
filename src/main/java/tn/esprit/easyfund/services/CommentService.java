package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.Comment;
import tn.esprit.easyfund.repositories.CommentRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CommentService {
    @Autowired
    CommentRepository CommentRepository ;

    public List<Comment> findAll() {
        return CommentRepository.findAll();
    }

    public Comment retrieveComment(Integer id) {
        return CommentRepository.findById(id).get();
    }
    public Comment add(Comment comment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Set the calendar's time to the current date

// Add 1 hour
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        comment.setDate(calendar.getTime());
        return CommentRepository.save(comment);
    }

    public void delete(Integer id) {
        CommentRepository.deleteById(id);

    }
    public Comment update(Integer id ,Comment Comment) {
        Optional<Comment> existingCommentOptional = CommentRepository.findById(id);

        if (existingCommentOptional.isPresent()) {
            Comment existingComment = existingCommentOptional.get();
            existingComment.setComment(Comment.getComment());

            return CommentRepository.save(existingComment);
        } else {
            return null;
        }
    }
}
