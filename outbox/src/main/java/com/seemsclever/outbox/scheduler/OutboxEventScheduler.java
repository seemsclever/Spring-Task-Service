package com.seemsclever.outbox.scheduler;

import com.seemsclever.outbox.domain.OutboxEvent;
import com.seemsclever.outbox.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
