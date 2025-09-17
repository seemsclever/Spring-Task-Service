package com.seemsclever.ports.controllers.dto;

import com.seemsclever.entities.TaskStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant startAt;
    private Instant endAt;
    private Instant expirationAt;

    private TaskStatus status;

    private Long userId;
}
