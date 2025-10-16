package com.seemsclever.outbox;

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

    @Column(name = "payload", length = 1000)
    private String payload;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "retry_time")
    private Instant retryTime;

    @Column(name = "type")
    private String type;

    public OutboxEvent(OutboxEventStatus status, String payload, Instant createdAt, Instant retryTime, String type) {
        this.status = status;
        this.payload = payload;
        this.createdAt = createdAt;
        this.retryTime = retryTime;
        this.type = type;
    }
}
