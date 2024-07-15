package com.Kuba2412.git_hub_repo.controller;

import com.Kuba2412.git_hub_repo.model.RepositoryDetails;
import com.Kuba2412.git_hub_repo.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GitHubRepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    public void testGetRepositoryDetails_Success() throws Exception {
        // Given
        RepositoryDetails details = new RepositoryDetails("Kuba2412/MedicalClinic", "Description", "https://github.com/Kuba2412/MedicalClinic.git", 10, "2024-05-12T17:22:40Z");
        given(repositoryService.getRepositoryDetails(anyString(), anyString())).willReturn(details);

        // When & Then
        mockMvc.perform(get("/repositories/Kuba2412/MedicalClinic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Kuba2412/MedicalClinic"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/Kuba2412/MedicalClinic.git"))
                .andExpect(jsonPath("$.stars").value(10));
    }

    @Test
    public void testGetRepositoryDetails_NotFound() throws Exception {
        // Given
        given(repositoryService.getRepositoryDetails(anyString(), anyString())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Repository not found"));

        // When & Then
        mockMvc.perform(get("/repositories/Kuba2412/NonExistingRepo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Repository not found"));
    }

    @Test
    public void testGetRepositoryDetails_InternalServerError() throws Exception {
        // Given
        given(repositoryService.getRepositoryDetails(anyString(), anyString())).willThrow(new RuntimeException("Internal server error"));

        // When & Then
        mockMvc.perform(get("/repositories/Kuba2412/AnyRepo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}