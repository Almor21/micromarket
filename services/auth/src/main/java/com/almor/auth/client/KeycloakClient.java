package com.almor.auth.client;

import com.almor.auth.dto.response.KeycloakTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KeycloakClient {

    @Value("${keycloak.url}")
    private String baseUrl;

    @Value("${keycloak.token-route}")
    private String tokenRoute;

    @Value("${keycloak.registration-route}")
    private String registerRoute;

    @Value("${keycloak.client-id}")
    private String clientId;

    private final RestClient.Builder restClientBuilder;

    public KeycloakClient() {
        this.restClientBuilder = RestClient.builder();
    }

    public String getToken(String username, String password) {
        RestClient restClient = restClientBuilder.baseUrl(baseUrl).build();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "password");
        data.add("client_id", clientId);
        data.add("username", username);
        data.add("password", password);

        KeycloakTokenResponseDto response = restClient.post()
                .uri(tokenRoute)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(data)
                .retrieve()
                .body(KeycloakTokenResponseDto.class);

        return response.getAccess_token();
    }

}
