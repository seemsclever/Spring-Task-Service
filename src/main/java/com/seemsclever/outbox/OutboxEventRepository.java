package com.seemsclever.outbox;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT e FROM OutboxEvent e
               WHERE e.retryTime <= :retryTime
                                       AND e.status = :status
                                                   ORDER BY e.retryTime ASC
            """)
    List<OutboxEvent> findUnsentEvents(Instant retryTime, OutboxEventStatus status, Pageable pageable);

}
