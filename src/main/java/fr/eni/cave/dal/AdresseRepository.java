package fr.eni.cave.dal;

import fr.eni.cave.bo.client.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdresseRepository extends JpaRepository<Adresse, Integer> {
}
