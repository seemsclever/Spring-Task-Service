package com.seemsclever.services;

import com.seemsclever.TaskKafkaProducer;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.entities.Task;
import com.seemsclever.repositories.TaskRepository;
import com.seemsclever.utils.MappingUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskKafkaProducer taskKafkaProducer;

    public TaskService(TaskRepository taskRepository, TaskKafkaProducer taskKafkaProducer) {
        this.taskRepository = taskRepository;
        this.taskKafkaProducer = taskKafkaProducer;
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll().stream()
                .map(MappingUtil::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id " + id));
        return MappingUtil.mapToTaskResponse(task);
    }

    public TaskResponse createTask(TaskRequest taskRequest){
        Task savedTask = taskRepository.save(MappingUtil.mapToTaskEntity(taskRequest));
        return MappingUtil.mapToTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id " + id));

        if(taskRequest.getTitle() != null){
            task.setTitle(taskRequest.getTitle());
        }
        if(taskRequest.getDescription() != null){
            task.setDescription(taskRequest.getDescription());
        }
        if(taskRequest.getStartAt() != null){
            task.setStartAt(taskRequest.getStartAt());
        }
        if(taskRequest.getEndAt() != null){
            task.setEndAt(taskRequest.getEndAt());
        }
        if(taskRequest.getExpirationAt() != null){
            task.setExpirationAt(taskRequest.getExpirationAt());
        }
        if(taskRequest.getStatus() != null){
            task.setStatus(taskRequest.getStatus());
        }
        if(taskRequest.getUserId() != null){
            task.setUserId(taskRequest.getUserId());
        }

        task.setUpdatedAt(Instant.now());

        Task updatedTask = taskRepository.save(task);
        return MappingUtil.mapToTaskResponse(updatedTask);
    }

    @Transactional
    public void updateTaskStatusById(Long id, TaskStatus taskStatus){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + id));
        task.setStatus(taskStatus);
        taskRepository.save(task);
        taskKafkaProducer.sendTaskToKafka(task);
    }

    public void deleteTaskById(Long id){
        taskRepository.deleteById(id);
    }
}
