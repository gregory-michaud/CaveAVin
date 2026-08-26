package fr.eni.cave.association;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.eni.cave.bo.vin.Bouteille;
import fr.eni.cave.bo.vin.Couleur;
import fr.eni.cave.bo.vin.Region;
import fr.eni.cave.dal.BouteilleRepository;
import fr.eni.cave.dal.CouleurRepository;
import fr.eni.cave.dal.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@Slf4j
@DataJpaTest
public class TestManyToOne {
	@Autowired
	BouteilleRepository bouteilleRepository;

	@Autowired
	CouleurRepository couleurRepository;
	
	@Autowired
	RegionRepository regionRepository;
	
	Couleur rougeDB;
	Couleur blancDB;
	Couleur roseDB;
	
	Region grandEstDB;
	Region paysDeLaLoireDB;
	Region nouvelleAquitaineDB;

	@BeforeEach
	public void initDB() {
		Couleur rouge = Couleur
				.builder()
				.nom("Rouge")
				.build();

		Couleur blanc = Couleur
				.builder()
				.nom("Blanc")
				.build();

		Couleur rose = Couleur
				.builder()
				.nom("Rosé")
				.build();

		rougeDB = couleurRepository.save(rouge);
		blancDB = couleurRepository.save(blanc);
		roseDB = couleurRepository.save(rose);

		Region grandEst =
				Region
				.builder()
				.nom("Grand Est")
				.build();

		Region paysDeLaLoire =
				Region
				.builder()
				.nom("Pays de la Loire")
				.build();

		Region nouvelleAquitaine =
				Region
				.builder()
				.nom("Nouvelle Aquitaine")
				.build();

		grandEstDB = regionRepository.save(grandEst);
		paysDeLaLoireDB = regionRepository.save(paysDeLaLoire);
		nouvelleAquitaineDB = regionRepository.save(nouvelleAquitaine);
	}

    @Test
    public void test_save() {
        final Bouteille bouteille = Bouteille
                .builder()
                .nom("Blanc du DOMAINE ENI Ecole")
                .millesime("2022")
                .prix(23.95f)
                .quantite(1298)
                .build();

        // Association ManyToOne
        bouteille.setRegion(paysDeLaLoireDB);
        bouteille.setCouleur(blancDB);

        // Appel du comportement
        final Bouteille bouteilleDB = bouteilleRepository.save(bouteille);
        // Vérification de l'identifiant
        assertThat(bouteilleDB.getId()).isGreaterThan(0);

        // Vérification des associations
        assertThat(bouteilleDB.getCouleur()).isNotNull();
        assertThat(bouteilleDB.getCouleur()).isEqualTo(blancDB);
        assertThat(bouteilleDB.getRegion()).isNotNull();
        assertThat(bouteilleDB.getRegion()).isEqualTo(paysDeLaLoireDB);
        log.info(bouteilleDB.toString());
    }

    @Test
    public void test_save_bouteilles_regions_couleurs() {
        final List<Bouteille> bouteilles = jeuDeDonnees();

		// bouteilleRepository.saveAll(bouteilles);

        // sauver le jeu de données en base
        bouteilles.forEach(b ->{
            Bouteille bDB = bouteilleRepository.save(b);
            assertThat(bDB.getId()).isGreaterThan(0);
            // Vérification des associations
            assertThat(bDB.getCouleur()).isNotNull();
            assertThat(bDB.getRegion()).isNotNull();
        });
        log.info(bouteilles.toString());
    }

    @Test
    public void test_delete() {
        final Bouteille bouteille = Bouteille
                .builder()
                .nom("Blanc du DOMAINE ENI Ecole")
                .millesime("2022")
                .prix(23.95f)
                .quantite(1298)
                .build();

        // Association ManyToOne
        bouteille.setRegion(paysDeLaLoireDB);
        bouteille.setCouleur(blancDB);

        // Appel du comportement
        final Bouteille bouteilleDB = bouteilleRepository.save(bouteille);
        // Vérification de l'identifiant
        assertThat(bouteilleDB.getId()).isGreaterThan(0);
        assertThat(bouteilleDB.getCouleur()).isNotNull();
        assertThat(bouteilleDB.getCouleur()).isEqualTo(blancDB);
        assertThat(bouteilleDB.getRegion()).isNotNull();
        assertThat(bouteilleDB.getRegion()).isEqualTo(paysDeLaLoireDB);

		Integer idBouteille = bouteilleDB.getId();

        // Appel du comportement
        bouteilleRepository.delete(bouteilleDB);

        // Vérification que l'entité a été supprimée
        Optional<Bouteille> optionalBouteille = bouteilleRepository.findById(idBouteille);
        assertThat(optionalBouteille).isEmpty();

        // Vérifier que les éléments associés sont toujours présents - PAS de cascade
        final List<Region> regions = regionRepository.findAll();
        assertThat(regions).isNotNull();
        assertThat(regions).isNotEmpty();
        assertThat(regions.size()).isEqualTo(3);
        final List<Couleur> couleurs = couleurRepository.findAll();
        assertThat(couleurs).isNotNull();
        assertThat(couleurs).isNotEmpty();
        assertThat(couleurs.size()).isEqualTo(3);
    }


    private List<Bouteille> jeuDeDonnees() {
		List<Bouteille> bouteilles = new ArrayList<>();
		bouteilles.add(Bouteille
				.builder()
				.nom("Blanc du DOMAINE ENI Ecole")
				.millesime("2022")
				.prix(23.95f)
				.quantite(1298)
				.region(paysDeLaLoireDB)
				.couleur(blancDB)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rouge du DOMAINE ENI Ecole")
				.millesime("2018")
				.prix(11.45f)
				.quantite(987)
				.region(paysDeLaLoireDB)
				.couleur(rougeDB)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Blanc du DOMAINE ENI Service")
				.millesime("2022")
				.prix(34)
				.petillant(true)
				.quantite(111)
				.region(grandEstDB)
				.couleur(blancDB)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rouge du DOMAINE ENI Service")
				.millesime("2012")
				.prix(8.15f)
				.quantite(344)
				.region(paysDeLaLoireDB)
				.couleur(rougeDB)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rosé du DOMAINE ENI")
				.millesime("2020")
				.prix(33)
				.quantite(1987)
				.region(nouvelleAquitaineDB)
				.couleur(roseDB)
				.build());
		return bouteilles;
	}
}
