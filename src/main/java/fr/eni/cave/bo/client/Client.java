package fr.eni.cave.bo.client;

import fr.eni.cave.bo.Utilisateur;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder

@Entity
@Table(name = "CAV_CLIENT")
public class Client extends Utilisateur  {


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private Adresse adresse;
}
