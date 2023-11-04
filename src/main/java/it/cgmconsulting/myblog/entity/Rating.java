package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class Rating extends CreationUpdate {

    @EmbeddedId
    private RatingId ratingId;

    @Check(constraints = "rate > 0 AND rate < 6")
    private byte rate; // voti da 1 (più basso) a 5 (più alto)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(ratingId, rating.ratingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingId);
    }
}
