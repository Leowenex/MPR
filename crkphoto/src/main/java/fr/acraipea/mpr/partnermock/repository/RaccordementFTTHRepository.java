package fr.acraipea.mpr.partnermock.repository;

import fr.acraipea.mpr.partnermock.entity.RaccordementFTTH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RaccordementFTTHRepository extends JpaRepository<RaccordementFTTH, String> {
    Optional<RaccordementFTTH> findByReferenceRaccordementAndCodeOI(String referenceRaccordement, String codeOI);
    Optional<RaccordementFTTH> findByRefenceCommandeAndCodeOI(String refenceCommande, String codeOI);
}
