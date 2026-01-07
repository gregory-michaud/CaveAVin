package fr.eni.cave.association;

import fr.eni.cave.bo.client.Adresse;
import fr.eni.cave.bo.client.Client;
import fr.eni.cave.dal.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestOneToOneUni {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository repository;

    @Test
    public void test_save() {
        final Adresse adresse = Adresse
                .builder()
                .rue("15 rue de Paris")
                .codePostal("35000")
                .ville("Rennes")
                .build();

        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .build();

        // Association
        client.setAdresse(adresse);

        // Appel du comportement
        final Client clientDB = repository.save(client);
        log.info(clientDB.toString());

        // Vérification de la cascade de l'association
        assertThat(clientDB.getAdresse()).isNotNull();
        assertThat(clientDB.getAdresse().getId()).isGreaterThan(0);
    }

    @Test
    public void test_delete() {
        final Adresse adresse = Adresse
                .builder()
                .rue("15 rue de Paris")
                .codePostal("35000")
                .ville("Rennes")
                .build();

        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .build();

        // Association
        client.setAdresse(adresse);

        // Contexte de la DB
        entityManager.persist(client);
        entityManager.flush();
        assertThat(adresse.getId()).isGreaterThan(0);

        // Appel du comportement
        repository.delete(client);

        // Vérification que l'entité a été supprimée
        Client clientDB = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB);
        Adresse adresseDB = entityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB);
    }

    @Test
    public void test_orphanRemoval() {
        final Adresse adresse = Adresse
                .builder()
                .rue("15 rue de Paris")
                .codePostal("35000")
                .ville("Rennes")
                .build();

        final Client client = Client
                .builder()
                .pseudo("bobeponge@email.fr")
                .password("carré")
                .nom("Eponge")
                .prenom("Bob")
                .build();

        // Association
        client.setAdresse(adresse);

        // Contexte de la DB
        entityManager.persist(client);
        entityManager.flush();
        assertThat(adresse.getId()).isGreaterThan(0);

        // Supprimer le lien entre l'entité Client et l'entité Adresse
        client.setAdresse(null);

        // Appel du comportement
        repository.delete(client);

        // Vérification que l'entité a été supprimée
        Client clientDB = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB);

        Adresse adresseDB = entityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB);
    }


}
