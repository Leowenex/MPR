package fr.acraipea.mpr.partnermock.exception;

import org.openapitools.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CriPhotoExceptionHandler {

    private final static String RACCORDEMENT_INCONNU = "RACCORDEMENT_INCONNU";
    private final static String PARTNER_ERROR = "PARTNER_ERROR";

    @ExceptionHandler(RaccordementInconnuException.class)
    ResponseEntity<ErrorResponse> handleRaccordementInconnuException(RaccordementInconnuException e) {
        ErrorResponse errorResponse = new ErrorResponse()
                .message(RACCORDEMENT_INCONNU)
                .description(e.getMessage())
                .code(404);
        return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PartnerException.class)
    ResponseEntity<ErrorResponse> handlePartnerException(PartnerException e) {
        ErrorResponse errorResponse = new ErrorResponse()
                .message(PARTNER_ERROR)
                .description(e.getMessage())
                .code(e.getStatus().value());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MappedPartnerException.class)
    ResponseEntity<ErrorResponse> handleMappedPartnerException(MappedPartnerException e) {
        ErrorResponse errorResponse = e.getErrorResponse();
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }
}
