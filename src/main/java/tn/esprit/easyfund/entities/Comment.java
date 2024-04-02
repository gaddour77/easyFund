package tn.esprit.easyfund.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Comment implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CommentId;
    @Column(nullable = false)
    private String comment;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Project project;

}