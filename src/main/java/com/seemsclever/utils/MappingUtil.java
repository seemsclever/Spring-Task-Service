package com.seemsclever.utils;

import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.entities.Task;
import org.springframework.stereotype.Service;

@Service
public class MappingUtil {
//    public static Task mapToTaskEntity(TaskRequest taskRequest){
//        Task entity = new Task();
//        entity.setTitle(taskRequest.getTitle());
//        entity.setDescription(taskRequest.getDescription());
//        entity.setStartAt(taskRequest.getStartAt());
//        entity.setEndAt(taskRequest.getEndAt());
//        entity.setExpirationAt(taskRequest.getExpirationAt());
//        entity.setStatus(taskRequest.getStatus());
//        entity.setUserId(taskRequest.getUserId());
//        return entity;
//    }
//
//    public static TaskResponse mapToTaskResponse(Task entity){
//        TaskResponse taskResponse = new TaskResponse();
//        taskResponse.setId(entity.getId());
//        taskResponse.setTitle(entity.getTitle());
//        taskResponse.setDescription(entity.getDescription());
//        taskResponse.setCreatedAt(entity.getCreatedAt());
//        taskResponse.setUpdatedAt(entity.getUpdatedAt());
//        taskResponse.setStartAt(entity.getStartAt());
//        taskResponse.setEndAt(entity.getEndAt());
//        taskResponse.setExpirationAt(entity.getExpirationAt());
//        taskResponse.setStatus(entity.getStatus());
//        taskResponse.setUserId(entity.getUserId());
//        return taskResponse;
//    }
}
