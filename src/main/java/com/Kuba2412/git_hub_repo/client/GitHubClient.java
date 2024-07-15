package com.Kuba2412.git_hub_repo.client;


import com.Kuba2412.git_hub_repo.fallback.GitHubClientFallBack;
import com.Kuba2412.git_hub_repo.repository.GitHubRepo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "githubClient", url = "https://api.github.com", configuration = GitHubClient.class, fallback = GitHubClientFallBack.class)
public interface GitHubClient {

    @GetMapping("/repos/{owner}/{repo}")
    GitHubRepo getRepository(@PathVariable("owner") String owner, @PathVariable("repo") String repo);
}