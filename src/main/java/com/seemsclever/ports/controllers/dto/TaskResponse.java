package com.seemsclever.ports.controllers.dto;

import com.seemsclever.entities.TaskStatus;

import java.time.Instant;

public record TaskResponse(
        Long id,
        String title,
        String titleOnTatar,
        String description,

        Instant createdAt,
        Instant updatedAt,
        Instant startAt,
        Instant endAt,
        Instant expirationAt,

        TaskStatus status,

        Long userId){

}