package com.seemsclever.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seemsclever.entities.OutboxEvent;
import com.seemsclever.entities.OutboxEventType;
import com.seemsclever.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OutboxEventMapper {

    @Mapping(source = "task", target = "payload", qualifiedByName = "convertTaskToJson")
    OutboxEvent toOutboxEvent(Task task, OutboxEventType type);

    @Named("convertTaskToJson")
    default String convertTaskToJson(Task task){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования Task в JSON", e);
        }
    }

    @Named("convertJsonToTask")
    default Task convertJsonToTask(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, Task.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования JSON в Task", e);
        }
    }
}
