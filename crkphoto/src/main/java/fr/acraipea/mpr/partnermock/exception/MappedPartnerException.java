package fr.acraipea.mpr.partnermock.exception;

import lombok.Getter;
import org.openapitools.model.ErrorResponse;
import org.springframework.http.HttpStatus;

public class MappedPartnerException extends PartnerException {

    @Getter
    private final ErrorResponse errorResponse;

    public MappedPartnerException(ErrorResponse errorResponse, HttpStatus status) {
        super(errorResponse.getMessage(), status);
        this.errorResponse = errorResponse;
    }
}
