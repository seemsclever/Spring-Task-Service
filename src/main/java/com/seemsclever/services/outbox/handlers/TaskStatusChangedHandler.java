package com.seemsclever.services.outbox.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seemsclever.entities.OutboxEventType;
import com.seemsclever.entities.Task;
import com.seemsclever.outbox.OutboxEventHandler;
import com.seemsclever.utils.TaskKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskStatusChangedHandler implements OutboxEventHandler {

    private final ObjectMapper objectMapper;
    private final TaskKafkaProducer taskKafkaProducer;

    @Override
    public String getType() {
        return OutboxEventType.TASK_STATUS_CHANGED.name();
    }

    @Override
    public void handle(String payload) {
        try {
            Task task = objectMapper.readValue(payload, Task.class);
            taskKafkaProducer.sendTaskToKafka(task);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
