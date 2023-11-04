package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
//@Table(name = "ruolo")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private byte id;

    @Column(length = 30, nullable = false, unique = true)
    private String authorityName; // authority_name

    private boolean visible = true;

    private boolean defaultAuthority = false;

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

}
