package fr.eni.cave.bo.vin;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 250)
    @Column(name = "NAME", unique = true, nullable = false, length = 250)
    private String nom;

    @Column(name = "SPARKLING")
    private boolean petillant;

    @Size(max = 100)
    @Column(name = "VINTAGE", length = 100)
    private String millesime;

    @Min(value = 1)
    @Column(name = "QUANTITY")
    private int quantite;

    @Min(value = 1)
    @Column(name = "PRICE", precision = 2)
    private float prix;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COLOR_ID")
    @EqualsAndHashCode.Exclude
    private Couleur couleur;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REGION_ID")
    @EqualsAndHashCode.Exclude
    private Region region;
}
