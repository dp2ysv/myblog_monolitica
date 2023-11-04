package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = 50)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Check(constraints = "type = 'H' or type = 'C' or type = 'F'")
    @Column(nullable = false)
    private char type;

    private byte prg; // progressivo per ordinamento di content di tipo 'C'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "content_images",
            joinColumns = {@JoinColumn(name = "content_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id", referencedColumnName = "id")})
    private Set<Image> images;

    public Content(String title, String content, char type, Post post) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.post = post;
    }
}
