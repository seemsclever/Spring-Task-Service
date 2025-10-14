package com.seemsclever.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "outboxevents")
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name="id", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private OutboxEventStatus status;

    @Column(name = "payload")
    private String payload;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "refresh_time")
    private Instant refreshTime;

    @Column(name = "type")
    private String type;

}
