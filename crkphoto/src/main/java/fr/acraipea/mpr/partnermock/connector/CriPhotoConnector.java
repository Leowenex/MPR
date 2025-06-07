package fr.acraipea.mpr.partnermock.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.acraipea.mpr.partnermock.config.CriPhotoConfiguration;
import fr.acraipea.mpr.partnermock.constants.MdcKeys;
import fr.acraipea.mpr.partnermock.exception.MappedPartnerException;
import fr.acraipea.mpr.partnermock.exception.PartnerException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.Photo;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriPhotoConnector {

    private final CriPhotoConfiguration criPhotoConfiguration;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private final static String ACCESS_URL_TEMPLATE = "%s/crk-photo/access/%s/photos";
    private final static String ORDER_URL_TEMPLATE = "%s/crk-photo/order/%s/photos";

    public List<Photo> getPhotosByAccess(String id, String codeOC) {
        String path = String.format(ACCESS_URL_TEMPLATE, criPhotoConfiguration.getOutGatewayUrl(), id);
        return getMetadata(path, codeOC);
    }

    public List<Photo> getPhotosByOrder(String id, String codeOC) {
        String path = String.format(ORDER_URL_TEMPLATE, criPhotoConfiguration.getOutGatewayUrl(), id);
        return getMetadata(path, codeOC);
    }

    private List<Photo> getMetadata(String url, String codeOC) {
        ParameterizedTypeReference<List<Photo>> typeRef = new ParameterizedTypeReference<>() {};

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Target-Partner", codeOC);
        headers.set("X-Client-Id", criPhotoConfiguration.getLocalPartnerCode());
        headers.set("X-Request-Id", MDC.get(MdcKeys.X_REQUEST_ID));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<Photo> photos = null;
        try {
            ResponseEntity<List<Photo>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, typeRef);
            photos = responseEntity.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            handleError(e.getResponseBodyAsString(), e.getStatusCode());
        }
        return Optional.ofNullable(photos).orElse(List.of());
    }

    private void handleError(String errorBody, HttpStatusCode status) {
        try {
            ErrorResponse errorResponse = objectMapper.readValue(errorBody, ErrorResponse.class);
            throw new MappedPartnerException(errorResponse, (HttpStatus) status);
        } catch (Exception e) {
            throw new PartnerException(errorBody, (HttpStatus) status);
        }
    }
}
