package com.seemsclever.services;

import com.seemsclever.entities.OutboxEvent;
import com.seemsclever.entities.OutboxEventType;
import com.seemsclever.entities.Task;
import com.seemsclever.mappers.OutboxEventMapper;
import com.seemsclever.repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventMapper outboxEventMapper;

    public void createOutboxEvent(Task task, OutboxEventType type){
        OutboxEvent outboxEvent = outboxEventMapper.toOutboxEvent(task, type);
        outboxEventRepository.save(outboxEvent);
    }

    public List<OutboxEvent> getUnsentOutboxEvents(){
        return outboxEventRepository.findUnsentEvents();
    }
}
