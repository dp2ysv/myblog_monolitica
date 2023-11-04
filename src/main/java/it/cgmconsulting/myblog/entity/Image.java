package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.configuration.ReadValueFromApplicationYaml;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter
    private int id;

    @Column(nullable = false)
    private String image; // rinominare l'immagine con un UUID in modo da evitare sovrascritture di file sulla cartella di rete

    public Image(String image) {
        this.image = image;
    }

    public String getImage() {
        return ReadValueFromApplicationYaml.IMAGE_PATH+image;
    }
}
