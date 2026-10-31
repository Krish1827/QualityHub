package com.qualityhub.model;

import java.time.LocalDateTime;

public record Task(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        LocalDateTime createdAt
) {}
