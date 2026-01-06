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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@Slf4j
public class TestClient {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository repository;

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

        // Contexte de la DB
        entityManager.persist(client);
        entityManager.flush();
        log.info(client.toString());

        // Appel du comportement
        repository.delete(client);

        // Vérification que l'entité a été supprimée
        Client clientDB = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB);
    }


}
