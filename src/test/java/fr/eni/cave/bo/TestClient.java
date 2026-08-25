package fr.eni.cave.bo;

import fr.eni.cave.bo.client.Client;
import fr.eni.cave.dal.ClientRepository;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Slf4j
public class TestClient {



    @Autowired
    private ClientRepository repository;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void test_save() {
        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .build();

        // Appel du comportement
        final Client clientDB = repository.save(client);
        log.info(clientDB.toString());

        // Vérification de la cascade de l'association
        assertThat(clientDB).isNotNull();
        assertThat(clientDB.equals(client)).isTrue();
    }

    @Test
    public void test_delete() {
        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .build();

        final Client clientDB = repository.save(client);
        log.info(clientDB.toString());

        // Appel du comportement
        repository.delete(clientDB);

        // Vérification que l'entité a été supprimée
        List<Client> listeClient = clientRepository.findAll();

        Client clientRecherche = listeClient
                .stream()
                .filter(c->c.getPseudo().equals(client.getPseudo()))
                .findAny()
                .orElse(null);
        assertNull(clientRecherche);
    }


}
