package fr.eni.cave.dal;

import fr.eni.cave.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    Utilisateur findByPseudo(String pseudo);
    Utilisateur findByPseudoAndPassword(String pseudo, String password);

}
