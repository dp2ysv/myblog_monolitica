package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.Creation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Comment extends Creation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String comment;

    private boolean censored = false;

    public Comment(User author, Post post, String comment) {
        this.author = author;
        this.post = post;
        this.comment = comment;
    }
}
