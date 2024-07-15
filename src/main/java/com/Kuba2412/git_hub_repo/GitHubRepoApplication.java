package com.Kuba2412.git_hub_repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GitHubRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitHubRepoApplication.class, args);
	}

}
