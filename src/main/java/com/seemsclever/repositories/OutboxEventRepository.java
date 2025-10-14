package com.seemsclever.repositories;

import com.seemsclever.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    @Query(value = """
            SELECT e FROM OutboxEvent e
               WHERE e.status IS NULL OR e.status <> 'DELIVERED'
            """)
    List<OutboxEvent> findUnsentEvents();

}
