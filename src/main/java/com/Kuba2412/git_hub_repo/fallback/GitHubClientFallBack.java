package com.Kuba2412.git_hub_repo.fallback;

import com.Kuba2412.git_hub_repo.client.GitHubClient;
import com.Kuba2412.git_hub_repo.repository.GitHubRepo;
import org.springframework.stereotype.Component;

public class GitHubClientFallBack {

    @Component
    public class GitHubClientFallback implements GitHubClient {
        @Override
        public GitHubRepo getRepository(String owner, String repo) {
            GitHubRepo fallbackRepo = new GitHubRepo();
            fallbackRepo.setFullName("Fallback Repo");
            fallbackRepo.setDescription("Default description");
            return fallbackRepo;
        }
    }
}