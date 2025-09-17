package com.seemsclever.ports.controllers.dto;

import com.seemsclever.entities.TaskStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class TaskRequest {

    private String title;
    private String description;

    private Instant startAt;
    private Instant endAt;
    private Instant expirationAt;

    private TaskStatus status;

    private Long userId;
}
