package fr.eni.cave.bo.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"pseudo"})
@ToString(of = {"pseudo", "nom", "prenom"})
@Builder

@Entity
@Table(name = "CAV_CLIENT")
public class Client {

    @Id
    @Column(name = "LOGIN", nullable = false, length = 255)
    private String pseudo;

    @Column(name = "PASSWORD", nullable = false, length = 68)
    private String password;

    @Column(name = "LAST_NAME", nullable = false, length = 90)
    private String nom;

    @Column(name = "FIRST_NAME", nullable = false, length = 150)
    private String prenom;
}
