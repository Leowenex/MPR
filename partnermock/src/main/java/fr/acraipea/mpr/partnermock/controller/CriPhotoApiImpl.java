package fr.acraipea.mpr.partnermock.controller;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.AccessApi;
import org.openapitools.api.OrderApi;
import org.openapitools.model.Photo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/crk-photo")
@Slf4j
public class CriPhotoApiImpl implements AccessApi, OrderApi {

    @Override
    public ResponseEntity<List<Photo>> getPhotosByAccess(String id, String xRequestId, String xClientId, String authorization) {
        log.info("getPhotosByAccess called with id: {}, xClientId: {}", id, xClientId);
        return ResponseEntity.ok(getMockResponse());
    }

    @Override
    public ResponseEntity<List<Photo>> getPhotosByOrder(String internalReference, String xRequestId, String xClientId, String authorization) {
        log.info("getPhotosByOrder called with internalReference: {}, xClientId: {}", internalReference, xClientId);
        return ResponseEntity.ok(getMockResponse());
    }

    private List<Photo> getMockResponse(){

        Photo photo1 = new Photo()
                .id(UUID.randomUUID())
                .name("Photo 1")
                .date(OffsetDateTime.now())
                .title("Titre 1")
                .description("Description 1")
                .type(Photo.TypeEnum.PTO)
                .mimeType(Photo.MimeTypeEnum.IMAGE_JPEG)
                .width(800)
                .height(600)
                .size(102400);

        Photo photo2 = new Photo()
                .id(UUID.randomUUID())
                .name("Photo 2")
                .date(OffsetDateTime.now())
                .title("Titre 2")
                .description("Description 2")
                .type(Photo.TypeEnum.PBO_AVANT)
                .mimeType(Photo.MimeTypeEnum.IMAGE_PNG)
                .width(1024)
                .height(768)
                .size(204800);

        Photo photo3 = new Photo()
                .id(UUID.randomUUID())
                .name("Photo 3")
                .date(OffsetDateTime.now())
                .title("Titre 3")
                .description("Description 3")
                .type(Photo.TypeEnum.PM_APRES)
                .mimeType(Photo.MimeTypeEnum.IMAGE_JPEG)
                .width(640)
                .height(480)
                .size(51200);

        return List.of(photo1, photo2, photo3);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return AccessApi.super.getRequest();
    }
}
