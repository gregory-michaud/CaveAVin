package fr.eni.cave.bo.vin;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder

@Entity
@Table(name = "CAV_BOTTLE")
public class Bouteille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOTTLE_ID")
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 250)
    private String nom;

    @Column(name = "SPARKLING")
    private boolean petillant;

    @Column(name = "VINTAGE", length = 100)
    private String millesime;

    @Column(name = "QUANTITY")
    private int quantite;

    @Column(name = "PRICE", precision = 2)
    private float prix;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COLOR_ID")
    @EqualsAndHashCode.Exclude
    private Couleur couleur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID")
    @EqualsAndHashCode.Exclude
    private Region region;
}
