package fr.eni.cave.bo;

import fr.eni.cave.bo.client.Client;
import fr.eni.cave.dal.ClientRepository;
import fr.eni.cave.dal.ProprioRepository;
import fr.eni.cave.dal.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class TestHeritage {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Autowired
	ProprioRepository proprioRepository;

	@Autowired
	ClientRepository clientRepository;

	@BeforeEach
	public void initDB() {
		List<Utilisateur> utilisateurs = new ArrayList<>();
		utilisateurs.add(Utilisateur
				.builder()
				.pseudo("harrisonford@email.fr")
				.password("IndianaJones3")
				.nom("Ford")
				.prenom("Harrison")
				.build());

		utilisateurs.add(Proprio
				.builder()
				.pseudo("georgelucas@email.fr")
				.password("Réalisateur&Producteur")
				.nom("Lucas")
				.prenom("George")
				.siret("12345678901234")
				.build());

		utilisateurs.add(Client
				.builder()
				.pseudo("natalieportman@email.fr")
				.password("MarsAttacks!")
				.nom("Portman")
				.prenom("Natalie")
				.build());

		// Contexte de la DB
		utilisateurs.forEach(e -> {
			entityManager.persist(e);
		});
	}

    @Test
    public void test_findAll_Utilisateur() {
        final List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        // Vérification
        assertThat(utilisateurs).isNotNull();
        assertThat(utilisateurs).isNotEmpty();
        assertThat(utilisateurs.size()).isEqualTo(3);
        assertThat(utilisateurs).hasSize(3);
        log.info(utilisateurs.toString());
    }

    @Test
    public void test_findAll_Proprio() {
        final List<Proprio> proprios = proprioRepository.findAll();

        // Vérification
        assertThat(proprios).isNotNull();
        assertThat(proprios).isNotEmpty();
        assertThat(proprios.size()).isEqualTo(1);
        log.info(proprios.toString());
    }

    @Test
    public void test_findAll_Client() {
        final List<Client> clients = clientRepository.findAll();

        // Vérification
        assertThat(clients).isNotNull();
        assertThat(clients).isNotEmpty();
        assertThat(clients.size()).isEqualTo(1);
        log.info(clients.toString());
    }

}
