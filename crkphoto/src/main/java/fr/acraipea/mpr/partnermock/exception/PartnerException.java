package fr.acraipea.mpr.partnermock.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class PartnerException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public PartnerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
