package com.seemsclever.ports.scheduler;

import com.seemsclever.entities.OutboxEvent;
import com.seemsclever.entities.Task;
import com.seemsclever.mappers.OutboxEventMapper;
import com.seemsclever.services.OutboxEventService;
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
    private final TaskKafkaProducer taskKafkaProducer;
    private final OutboxEventMapper outboxEventMapper;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void produceUnsentEvents(){
        List<OutboxEvent> unsentEvents = outboxEventService.getUnsentOutboxEvents();

        unsentEvents.forEach(event -> {
            // handler, EventHandlerFactoryService

//            Task task = outboxEventMapper.convertJsonToTask(event);
//            taskKafkaProducer.sendTaskToKafka(event);
                }
                );
    }

}
