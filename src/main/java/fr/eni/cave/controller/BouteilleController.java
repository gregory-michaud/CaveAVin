package fr.eni.cave.controller;

import fr.eni.cave.bll.BouteilleService;
import fr.eni.cave.bo.vin.Bouteille;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor

@RestController
@RequestMapping("/caveavin/bouteilles")
public class BouteilleController {

    private BouteilleService bService;

    @GetMapping
    public ResponseEntity<?> rechercherToutesBouteilles(){

        List<Bouteille> bouteilles = bService.chargerToutesBouteilles();
        if(bouteilles == null || bouteilles.isEmpty()){
            // Statut 204 : No Content - Pas de body car rien à afficher
            return ResponseEntity.noContent().build();
        }
        // Statut 200 : OK + dans le body bouteilles
        // Le contenu du body est directement injecté dans la méthode ok
        return ResponseEntity.ok(bouteilles);
    }
}
