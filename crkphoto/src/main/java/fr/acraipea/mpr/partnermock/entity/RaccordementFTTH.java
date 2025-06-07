package fr.acraipea.mpr.partnermock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaccordementFTTH {

    @Id
    private String referenceRaccordement;

    private String refenceCommande;

    @Column(length = 4)
    private String codeOI;

    @Column(length = 4)
    private String codeOC;
}
