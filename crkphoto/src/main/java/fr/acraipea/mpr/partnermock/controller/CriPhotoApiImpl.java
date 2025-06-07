package fr.acraipea.mpr.partnermock.controller;

import fr.acraipea.mpr.partnermock.constants.MdcKeys;
import fr.acraipea.mpr.partnermock.service.CriPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AccessApi;
import org.openapitools.api.OrderApi;
import org.openapitools.model.Photo;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/crk-photo")
@RequiredArgsConstructor
@Slf4j
public class CriPhotoApiImpl implements AccessApi, OrderApi {

    private final CriPhotoService criPhotoService;

    @Override
    public ResponseEntity<List<Photo>> getPhotosByAccess(String id, String xRequestId, String xClientId, String authorization) {
        MDC.put(MdcKeys.X_REQUEST_ID, xRequestId);
        log.info("getPhotosByAccess called with id: {}, xClientId: {}", id, xClientId);
        return ResponseEntity.ok(criPhotoService.getPhotosByAccess(id, xClientId));
    }

    @Override
    public ResponseEntity<List<Photo>> getPhotosByOrder(String internalReference, String xRequestId, String xClientId, String authorization) {
        MDC.put("X-Request-Id", xRequestId);
        log.info("getPhotosByOrder called with internalReference: {}, xClientId: {}", internalReference, xClientId);
        return ResponseEntity.ok(criPhotoService.getPhotosByOrder(internalReference, xClientId));
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return AccessApi.super.getRequest();
    }
}
