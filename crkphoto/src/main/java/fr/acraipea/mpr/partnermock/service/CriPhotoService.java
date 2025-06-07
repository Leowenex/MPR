package fr.acraipea.mpr.partnermock.service;

import fr.acraipea.mpr.partnermock.connector.CriPhotoConnector;
import fr.acraipea.mpr.partnermock.entity.RaccordementFTTH;
import fr.acraipea.mpr.partnermock.exception.RaccordementInconnuException;
import fr.acraipea.mpr.partnermock.repository.RaccordementFTTHRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.Photo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CriPhotoService {

    private final CriPhotoConnector criPhotoConnector;
    private final RaccordementFTTHRepository raccordementFTTHRepository;

    public List<Photo> getPhotosByAccess(String id, String clientId) {
        Optional<RaccordementFTTH> raccordement = raccordementFTTHRepository.findByReferenceRaccordementAndCodeOI(id, clientId);
        if (raccordement.isPresent()) {
            String codeOC = raccordement.get().getCodeOC();
            return criPhotoConnector.getPhotosByAccess(id, codeOC);
        } else {
            log.error("Raccordement not found for access id: {}", id);
            throw new RaccordementInconnuException("Raccordement not found for access id: " + id);
        }
    }

    public List<Photo> getPhotosByOrder(String id, String clientId) {
        Optional<RaccordementFTTH> raccordement = raccordementFTTHRepository.findByRefenceCommandeAndCodeOI(id, clientId);
        if (raccordement.isPresent()) {
            String codeOC = raccordement.get().getCodeOC();
            return criPhotoConnector.getPhotosByOrder(id, codeOC);
        } else {
            log.error("Raccordement not found for order id: {}", id);
            throw new RaccordementInconnuException("Raccordement not found for order id: " + id);
        }
    }
}
