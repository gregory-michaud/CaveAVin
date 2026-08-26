package fr.eni.cave.dal;

import fr.eni.cave.bo.client.LignePanier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LignePanierRepository extends JpaRepository<LignePanier,Integer> {
}
