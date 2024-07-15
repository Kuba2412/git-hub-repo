package com.Kuba2412.git_hub_repo.controller;

import com.Kuba2412.git_hub_repo.model.RepositoryDetails;
import com.Kuba2412.git_hub_repo.service.RepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/repositories")
public class GitHubRepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @Operation(summary = "Get repository details", description = "Fetch the details of a GitHub repository")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved repository details"),
            @ApiResponse(responseCode = "404", description = "Repository not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{owner}/{repositoryname}")
    public RepositoryDetails getRepositoryDetails(@PathVariable("owner") String owner, @PathVariable("repositoryname") String repositoryname, Locale locale) {
        return repositoryService.getRepositoryDetails(owner, repositoryname);
    }
}