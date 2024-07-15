package com.Kuba2412.git_hub_repo.service;


import com.Kuba2412.git_hub_repo.client.GitHubClient;
import com.Kuba2412.git_hub_repo.model.RepositoryDetails;
import com.Kuba2412.git_hub_repo.repository.GitHubRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

public class RepositoryServiceTest {

    @Mock
    private GitHubClient gitHubClient;

    private RepositoryService repositoryService;

    @Test
    public void testGetRepositoryDetails_Positive() {
        // Given
        GitHubRepo repo = new GitHubRepo();
        repo.setFullName("Kuba2412/Medicalclinic ");
        repo.setDescription("Description");
        repo.setCloneUrl("https://github.com/Kuba2412/MedicalClinic.git");
        repo.setStars(100);
        repo.setCreatedAt(LocalDateTime.parse("2023-01-01T10:00"));

        given(gitHubClient.getRepository(anyString(), anyString())).willReturn(repo);

        // When
        RepositoryDetails details = repositoryService.getRepositoryDetails("Kuba2412", "Medicalclinic");

        // Then
        assertEquals("Kuba2412/MedicalClinic", details.getFullName());
        assertEquals("Description", details.getDescription());
        assertEquals("https://github.com/Kuba2412/MedicalClinic.git", details.getCloneUrl());
        assertEquals(10, details.getStars());
        assertEquals("2023-01-01T10:00", details.getCreatedAt());
    }

    @Test
    public void testGetRepositoryDetails_NotFound() {
        // Given
        given(gitHubClient.getRepository(anyString(), anyString())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Repository not found"));

        // When & Then
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            repositoryService.getRepositoryDetails("Kuba2412", "NonExistingRepo");
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("Repository not found", thrown.getReason());
    }
}