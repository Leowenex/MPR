package fr.acraipea.mpr.partnermock.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Raccordement inconnu")
public class RaccordementInconnuException extends RuntimeException {

    public RaccordementInconnuException(String message) {
        super(message);
    }
}
