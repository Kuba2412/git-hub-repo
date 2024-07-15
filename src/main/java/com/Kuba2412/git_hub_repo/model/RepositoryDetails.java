package com.Kuba2412.git_hub_repo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDetails {
    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private String createdAt;
}