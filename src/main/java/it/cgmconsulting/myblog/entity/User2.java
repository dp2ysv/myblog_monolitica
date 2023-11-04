package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "user2")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder

public class User2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 15, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}
