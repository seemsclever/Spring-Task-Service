package com.seemsclever.ports.controllers.dto;

import com.seemsclever.entities.TaskStatus;

import java.time.Instant;

public record TaskRequest(
        String title,
        String title_on_tatar,
        String description,

        Instant startAt,
        Instant endAt,
        Instant expirationAt,

        TaskStatus status,

        Long userId) {
}
