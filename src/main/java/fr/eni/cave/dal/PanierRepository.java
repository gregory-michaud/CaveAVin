package fr.eni.cave.dal;

import fr.eni.cave.bo.client.Client;
import fr.eni.cave.bo.client.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PanierRepository extends JpaRepository<Panier,Integer> {

    List<Panier> findByClientAndNumCommandeNull(Client client);

    @Query("SELECT p FROM Panier p WHERE p.client = :client AND p.numCommande IS NULL")
    List<Panier> findPaniersWithJPQL(@Param("client") Client client);

    List<Panier> findByClientAndNumCommandeNotNull(Client client);

    @Query(value = "SELECT p.* FROM CAV_SHOPPING_CART p WHERE p.CLIENT_ID = :idClient AND p.ORDER_NUMBER IS NOT NULL", nativeQuery = true)
    List<Panier> findCommandesWithSQL(@Param("idClient") String idClient);

}
