package com.almor.auth.client;

import com.almor.auth.client.dto.AdminRegisterUserDto;
import com.almor.auth.dto.response.KeycloakTokenResponseDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private RestClient restClient;

    @PostConstruct
    public void init() {
        this.restClient = RestClient.create(baseUrl);
    }

    private String getClientToken() {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "client_credentials");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);

        KeycloakTokenResponseDto response = restClient.post()
                .uri(tokenRoute)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(data)
                .retrieve()
                .body(KeycloakTokenResponseDto.class);

        return response.getAccess_token();
    }

    public String getUserToken(String username, String password) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "password");
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
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

    public void register(AdminRegisterUserDto data) {
        String adminToken = getClientToken();

        restClient.post()
                .uri(registerRoute)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }

}
