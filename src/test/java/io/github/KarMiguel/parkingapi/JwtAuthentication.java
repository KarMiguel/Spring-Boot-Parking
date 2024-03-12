package io.github.KarMiguel.parkingapi;


import io.github.KarMiguel.parkingapi.jwt.JwtToken;
import io.github.KarMiguel.parkingapi.web.dto.UserLoginDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UserLoginDTO(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();
        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}

