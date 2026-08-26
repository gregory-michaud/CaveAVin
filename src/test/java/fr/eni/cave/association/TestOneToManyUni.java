package fr.eni.cave.association;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.eni.cave.dal.LignePanierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.eni.cave.bo.client.LignePanier;
import fr.eni.cave.bo.client.Panier;
import fr.eni.cave.dal.PanierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

@Slf4j
@DataJpaTest
public class TestOneToManyUni {


	@Autowired
	private PanierRepository panierRepository;
    @Autowired
    private LignePanierRepository lignePanierRepository;

    @Test
    public void test_save_nouvelleLigne_nouveauPanier() {
        int qte = 4;
        final LignePanier lp = LignePanier
                .builder()
                .quantite(qte)
                .prix(qte * 23.95f)
                .build();

        final Panier panier = new Panier();
        panier.setPrixTotal(lp.getPrix());

        // Association OneToMany
        panier.getLignesPanier().add(lp);

        // Appel du comportement
        final Panier panierDB = panierRepository.save(panier);
        // Vérification de l'identifiant de l'entreprise
        assertThat(panierDB.getId()).isGreaterThan(0);

        // Vérification de la cascade de l'association
        assertThat(panierDB.getLignesPanier()).isNotNull();
        assertThat(panierDB.getLignesPanier()).isNotEmpty();
        assertThat(panierDB.getLignesPanier().size()).isEqualTo(1);
        log.info(panierDB.toString());
    }

    @Test
    public void test_save_nouvelleLigne_Panier() {
        final Panier panierDB = panierEnDB();
        int qte = 10;

        final LignePanier lp = LignePanier
                .builder()
                .quantite(qte)
                .prix(qte * 23.95f)
                .build();

        panierDB.setPrixTotal(panierDB.getPrixTotal() + lp.getPrix());

        // Association OneToMany
        panierDB.getLignesPanier().add(lp);

        // Appel du comportement
        final Panier panierDB2 = panierRepository.save(panierDB);
        // Vérification de l'identifiant de l'entreprise
        assertThat(panierDB2.getId()).isEqualTo(panierDB.getId());

        // Vérification de la cascade de l'association
        assertThat(panierDB2.getLignesPanier()).isNotNull();
        assertThat(panierDB2.getLignesPanier()).isNotEmpty();
        assertThat(panierDB2.getLignesPanier().size()).isEqualTo(2);
        log.info(panierDB2.toString());
    }

    @Test
    public void test_delete() {
        final Panier panierDB = panierEnDB();
        final List<Integer> listeIdLignePanier = panierDB.getLignesPanier()
                .stream()
                .map(LignePanier::getId)
                .collect(Collectors.toList());

        Integer idPanier = panierDB.getId();
        // Appel du comportement
        panierRepository.delete(panierDB);

        // Vérification que l'entité a été supprimée
        Optional<Panier> optionalPanier = panierRepository.findById(idPanier);

        assertThat(optionalPanier).isEmpty();

        // Vérifier que tous les LignePanier sont supprimés par cascade
        assertThat(listeIdLignePanier).isNotNull();
        assertThat(listeIdLignePanier).isNotEmpty();
        listeIdLignePanier.forEach(id -> {
            assertThat(id).isGreaterThan(0);
            Optional<LignePanier> optionalLignePanier = lignePanierRepository.findById(id);
            assertThat(optionalLignePanier).isEmpty();
        });
    }

    @Test
    public void test_orphanRemoval() {
        final Panier panierDB = panierEnDB();
        final List<Integer> listeIdLignePanier = panierDB.getLignesPanier()
                .stream()
                .map(LignePanier::getId)
                .collect(Collectors.toList());

        // Détacher les LignePanier
        panierDB.getLignesPanier().clear();

        Integer idPanier = panierDB.getId();
        // Appel du comportement
        panierRepository.delete(panierDB);

        // Vérification que l'entité a été supprimée
        Optional<Panier> optionalPanier = panierRepository.findById(idPanier);
        assertThat(optionalPanier).isEmpty();

        // Vérifier que tous les LignePanier sont supprimées par orphanRemoval
        assertThat(listeIdLignePanier).isNotNull();
        assertThat(listeIdLignePanier).isNotEmpty();
        listeIdLignePanier.forEach(id -> {
            assertThat(id).isGreaterThan(0);
            Optional<LignePanier> optionalLignePanier = lignePanierRepository.findById(id);
            assertThat(optionalLignePanier).isEmpty();
        });
    }


	private Panier panierEnDB() {
		final Panier panier = new Panier();
		final LignePanier lp = LignePanier
				.builder()
				.quantite(3)
				.prix(3 * 11.45f)
				.build();
		panier.getLignesPanier().add(lp);
		panier.setPrixTotal(lp.getPrix());

		Panier panierDB = panierRepository.save(panier);

		assertThat(panierDB.getId()).isGreaterThan(0);
		assertThat(panierDB.getId()).isGreaterThan(0);

		return panierDB;
	}
}
