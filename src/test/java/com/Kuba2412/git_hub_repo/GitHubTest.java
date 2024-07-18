package com.Kuba2412.git_hub_repo;

import com.Kuba2412.git_hub_repo.client.GitHubClient;
import com.Kuba2412.git_hub_repo.repository.GitHubRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class GitHubTest {
    private static WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @Autowired
    private ObjectMapper objectMapper;

    public static void setup() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
    }


    @Test
    public void testGetRepositoryDetails_Success() throws Exception {
        // Given
        GitHubRepo repo = new GitHubRepo();
        repo.setFullName("Kuba2412/MedicalClinic");
        repo.setDescription("Description");
        repo.setCloneUrl("https://github.com/Kuba2412/MedicalClinic.git");
        repo.setStars(10);
        repo.setCreatedAt(LocalDateTime.now());

        String repoJson = objectMapper.writeValueAsString(repo);

        wireMockServer.stubFor(get(urlPathEqualTo("/repos/Kuba2412/MedicalClinic"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(repoJson)));

        // When
        GitHubRepo responseRepo = gitHubClient.getRepository("Kuba2412", "MedicalClinic");

        // Then
        assertEquals(repo.getFullName(), responseRepo.getFullName());
        assertEquals(repo.getDescription(), responseRepo.getDescription());
        assertEquals(repo.getCloneUrl(), responseRepo.getCloneUrl());
        assertEquals(repo.getStars(), responseRepo.getStars());
        assertEquals(repo.getCreatedAt(), responseRepo.getCreatedAt());
    }

    @Test
    public void testGetRepositoryDetails_NotFound() {
        // Given
        wireMockServer.stubFor(get(urlPathEqualTo("/repos/Kuba2412/NonExistingRepo"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Not Found\"}")));

        // When & Then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            gitHubClient.getRepository("Kuba2412", "NonExistingRepo");
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    }

    @Test
    public void testGetRepositoryDetails_ServiceUnavailable() {
        // Given
        wireMockServer.stubFor(get(urlPathEqualTo("/repos/Kuba2412/AnyRepo"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Service Unavailable\"}")));

        // When & Then
        RetryableException thrown = assertThrows(RetryableException.class, () -> {
            gitHubClient.getRepository("Kuba2412", "AnyRepo");
        });
        assertEquals(503, thrown.status());
    }
}