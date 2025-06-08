package fr.acraipea.mpr.partnermock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthApi {

    @PostMapping("/bearer-token")
    public ResponseEntity<Map<String, String>> bearerToken() {
        return ResponseEntity.ok(Map.of(
                "token", UUID.randomUUID().toString(),
                "expires_in", "3600"
        ));
    }

}
