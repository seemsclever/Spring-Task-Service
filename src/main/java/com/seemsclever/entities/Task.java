package com.seemsclever.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name="tasks")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description")
    private String description;


    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name="start_at", nullable = false)
    private Instant startAt;

    @Column(name="end_at", nullable = false)
    private Instant endAt;

    @Column(name="expiration_at", nullable = false)
    private Instant expirationAt;


    @Column(name="status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Builder
    public Task(String title, String description, Instant createdAt, Instant updatedAt, Instant startAt, Instant endAt, Instant expirationAt, TaskStatus status, Long userId) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.expirationAt = expirationAt;
        this.status = status;
        this.userId = userId;
    }
}



