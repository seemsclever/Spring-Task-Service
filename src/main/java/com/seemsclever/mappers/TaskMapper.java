package com.seemsclever.mappers;

import com.seemsclever.entities.Task;
import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskResponse toTaskResponse(Task task);

    Task toTaskEntity(TaskRequest taskRequest);

    List<TaskResponse> toTaskResponseList(List<Task> tasks);

    List<Task> toTaskEntityList(List<TaskRequest> taskRequests);

}