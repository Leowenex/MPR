package fr.acraipea.mpr.partnermock.config;

import fr.acraipea.mpr.partnermock.entity.RaccordementFTTH;
import fr.acraipea.mpr.partnermock.repository.RaccordementFTTHRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CriPhotoDataLoad implements CommandLineRunner {

    private final RaccordementFTTHRepository raccordementFTTHRepository;

    @Override
    public void run(String... args) {
        List<RaccordementFTTH> raccordements = List.of(
                RaccordementFTTH.builder()
                        .referenceRaccordement("1234567890")
                        .refenceCommande("CMD123456")
                        .codeOI("OI01")
                        .codeOC("OC01")
                        .build(),
                RaccordementFTTH.builder()
                        .referenceRaccordement("0987654321")
                        .refenceCommande("CMD654321")
                        .codeOI("OI02")
                        .codeOC("OC02")
                        .build()
        );
        raccordementFTTHRepository.saveAll(raccordements);

        log.info("Test data loaded into RaccordementFTTH repository: {}", raccordementFTTHRepository.count());
    }
}
