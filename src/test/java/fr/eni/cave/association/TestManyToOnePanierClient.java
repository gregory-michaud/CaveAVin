package fr.eni.cave.association;

import fr.eni.cave.bo.client.Adresse;
import fr.eni.cave.bo.client.Client;
import fr.eni.cave.bo.client.LignePanier;
import fr.eni.cave.bo.client.Panier;
import fr.eni.cave.bo.vin.Bouteille;
import fr.eni.cave.bo.vin.Couleur;
import fr.eni.cave.bo.vin.Region;
import fr.eni.cave.dal.BouteilleRepository;
import fr.eni.cave.dal.ClientRepository;
import fr.eni.cave.dal.PanierRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestManyToOnePanierClient {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	PanierRepository panierRepository;

	@Autowired
	BouteilleRepository bouteilleRepository;
	
	@Autowired
	ClientRepository clientRepository;

	@BeforeEach
	public void initDB() {
		final List<Couleur> couleurs = new ArrayList<>();
		couleurs.add(Couleur
				.builder()
				.nom("Blanc")
				.build());
		couleurs.add(Couleur
				.builder()
				.nom("Rouge")
				.build());

		couleurs.forEach(item -> {
			entityManager.persist(item);
			assertThat(item.getId()).isGreaterThan(0);
		});
		entityManager.flush();

		final List<Region> regions = new ArrayList<>();
		regions.add(Region
				.builder()
				.nom("Pays de la Loire")
				.build());

		regions.add(Region
				.builder()
				.nom("Grand Est")
				.build());

		regions.forEach(item -> {
			entityManager.persist(item);
			assertThat(item.getId()).isGreaterThan(0);
		});
		entityManager.flush();

		final List<Bouteille> bouteilles = new ArrayList<>();
		bouteilles.add(Bouteille
				.builder()
				.nom("DOMAINE ENI Ecole")
				.millesime("2022")
				.prix(11.45f)
				.quantite(1298)
				.region(regions.get(0))
				.couleur(couleurs.get(0))
				.build());

		bouteilles.add(Bouteille
				.builder()
				.nom("DOMAINE ENI Service")
				.millesime("2015")
				.prix(23.95f)
				.quantite(2998)
				.region(regions.get(1))
				.couleur(couleurs.get(1))
				.build());

		bouteilles.forEach(item -> {
			entityManager.persist(item);
			assertThat(item.getId()).isGreaterThan(0);
		});
		entityManager.flush();
	}


    @Test
    public void test_save_1panier() {
        final List<Bouteille> bouteilles = bouteilleRepository.findAll();
        assertThat(bouteilles).isNotNull();
        assertThat(bouteilles).isNotEmpty();
        assertThat(bouteilles.size()).isEqualTo(2);

        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .adresse(Adresse
                        .builder()
                        .rue("Sous la mer")
                        .codePostal("35000")
                        .ville("Rennes")
                        .build())
                .build();

        final Panier panier = new Panier();
        final Bouteille b1 = bouteilles.get(0);
        int qte = 3;
        final LignePanier lp1 = LignePanier
                .builder()
                .bouteille(b1)
                .quantite(qte)
                .prix(qte * b1.getPrix())
                .build();
        panier.getLignesPanier().add(lp1);
        panier.setPrixTotal(lp1.getPrix());

        // Association ManyToOne
        panier.setClient(client);

        // Appel du comportement
        final Panier panierDB = panierRepository.save(panier);
        // Vérification de l'identifiant
        assertThat(panierDB.getId()).isGreaterThan(0);
        assertThat(panierDB.getClient()).isNotNull();
        final Client clientDB = panierDB.getClient();
        assertThat(clientDB.getPseudo()).isNotNull();
        log.info(panierDB.toString());
    }

    @Test
    public void test_save_paniers_unClient() {
        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .adresse(Adresse
                        .builder()
                        .rue("Sous la mer")
                        .codePostal("35000")
                        .ville("Rennes")
                        .build())
                .build();

        // Association ManyToOne
        final List<Panier> paniers = jeuDeDonnees();
        paniers.forEach(p -> {
            p.setClient(client);
        });

        // Contexte de la DB
        paniers.forEach(p -> {
            final Panier panierDB = panierRepository.save(p);
            assertThat(panierDB.getId()).isGreaterThan(0);
            assertThat(panierDB.getClient()).isNotNull();
            final Client clientDB = panierDB.getClient();
            assertThat(clientDB.getPseudo()).isNotNull();
        });

        log.info(paniers.toString());
    }

    @Test
    public void test_delete() {
        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .adresse(Adresse
                        .builder()
                        .rue("Sous la mer")
                        .codePostal("35000")
                        .ville("Rennes")
                        .build())
                .build();

        // Association ManyToOne
        final List<Panier> paniers = jeuDeDonnees();
        paniers.forEach(p -> {
            p.setClient(client);
        });

        // Contexte de la DB
        paniers.forEach(p -> {
            entityManager.persist(p);
            assertThat(p.getId()).isGreaterThan(0);
        });
        entityManager.flush();

        // Récupération des identifiants des paniers pour le test de suppression
        List<Integer> listeIdPanier = paniers
                .stream()
                .map(Panier::getId)
                .collect(Collectors.toList());
        assertThat(listeIdPanier).isNotNull();
        assertThat(listeIdPanier).isNotEmpty();
        assertThat(listeIdPanier.size()).isEqualTo(2);

        // Appel du comportement
        clientRepository.delete(client);

        // Vérification que l'entité a été supprimée
        final Client clientDB1 = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB1);

        // Les entités Panier doivent être en base
        // Il n'y a pas la notion de DELETE sur notre relation
        assertThat(listeIdPanier).isNotNull();
        assertThat(listeIdPanier).isNotEmpty();
        listeIdPanier.forEach(id -> {
            assertThat(id).isGreaterThan(0);
            Panier panierDB = entityManager.find(Panier.class, id);
            assertNotNull(panierDB);
        });
    }

    private List<Panier> jeuDeDonnees() {
		final List<Bouteille> bouteilles = bouteilleRepository.findAll();
		assertThat(bouteilles).isNotNull();
		assertThat(bouteilles).isNotEmpty();
		assertThat(bouteilles.size()).isEqualTo(2);

		final List<Panier> paniers = new ArrayList<>();

		final Panier p1 = new Panier();
		int qte1 = 3;
		final Bouteille b1 = bouteilles.get(0);
		final LignePanier lp1 = LignePanier
				.builder()
				.bouteille(b1)
				.quantite(qte1)
				.prix(qte1 * b1.getPrix())
				.build();
		p1.getLignesPanier().add(lp1);
		p1.setPrixTotal(lp1.getPrix());
		paniers.add(p1);

		final Panier p2 = new Panier();
		int qte2 = 10;
		final Bouteille b2 = bouteilles.get(1);
		final LignePanier lp2 = LignePanier
				.builder()
				.bouteille(b2)
				.quantite(qte2)
				.prix(qte2 * b2.getPrix())
				.build();
		p2.getLignesPanier().add(lp2);
		p2.setPrixTotal(lp2.getPrix());
		paniers.add(p2);

		return paniers;
	}
}
