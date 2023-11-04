package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String filename;

    @Column(length = 50, nullable = false)
    private String filetype; // mime type (es. image/png)

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] data;

    public Avatar(String filename, String filetype, byte[] data) {
        this.filename = filename;
        this.filetype = filetype;
        this.data = data;
    }
}
