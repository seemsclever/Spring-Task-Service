package com.seemsclever.ports.controllers.dto;

import com.seemsclever.entities.TaskStatus;

import java.time.Instant;

public record TaskRequest(
        String title,
        String titleOnTatar,
        String description,

        Instant startAt,
        Instant endAt,
        Instant expirationAt,

        TaskStatus status,

        Long userId) {
}
