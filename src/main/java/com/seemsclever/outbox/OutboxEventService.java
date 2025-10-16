package com.seemsclever.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seemsclever.entities.OutboxEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final OutboxEventFactory outboxEventFactory;

    @Value("${outbox.limit}")
    private Integer limit;
    @Value("${outbox.timeoutInSeconds}")
    private Integer timeoutInSeconds;

    @Transactional
    public void createOutboxEvent(Object object, String type){
        Instant now = Instant.now();
        OutboxEvent outboxEvent = null;
        try {
            outboxEvent = new OutboxEvent(OutboxEventStatus.IN_PROGRESS, objectMapper.writeValueAsString(object), now, now, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional
    public List<OutboxEvent> getUnsentOutboxEvents(){
        Pageable pageable = PageRequest.of(0, limit);
        Instant currentTime = Instant.now();
        List<OutboxEvent> outboxEvents = outboxEventRepository.findUnsentEvents(currentTime, OutboxEventStatus.IN_PROGRESS, pageable);
        outboxEvents.forEach(oe ->{
            oe.setRetryTime(currentTime.plus(Duration.ofSeconds(timeoutInSeconds)));
        });
        return outboxEvents;
    }

    @Transactional
    public void handleOutboxEvent(OutboxEvent event){
        OutboxEventHandler handler = outboxEventFactory.getHandler(event.getType());
        handler.handle(event.getPayload());
        event.setStatus(OutboxEventStatus.DELIVERED);
        outboxEventRepository.save(event);
    }
}
