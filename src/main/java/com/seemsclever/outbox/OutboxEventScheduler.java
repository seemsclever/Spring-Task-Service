package com.seemsclever.outbox;

import com.seemsclever.utils.TaskKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventScheduler {

    private final OutboxEventService outboxEventService;

    @Scheduled(fixedRate = 5000)
    public void produceUnsentEvents(){
        List<OutboxEvent> unsentEvents = outboxEventService.getUnsentOutboxEvents();

        unsentEvents.forEach(outboxEventService::handleOutboxEvent);
    }

}
