package com.seemsclever.services;

import com.seemsclever.entities.OutboxEventType;
import com.seemsclever.utils.TaskKafkaProducer;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.mappers.TaskMapper;
import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.entities.Task;
import com.seemsclever.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskKafkaProducer taskKafkaProducer;
    private final TaskMapper taskMapper;
    private final TranslationService translationService;
    private final OutboxEventService outboxEventService;

    public List<TaskResponse> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toTaskResponseList(tasks);
    }

    public TaskResponse getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id " + id));
        return taskMapper.toTaskResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest){
        Task task = taskMapper.toTaskEntity(taskRequest);

        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        task.setTitleOnTatar(translationService.translateToTatarLang(task.getTitle()));

        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id " + id));

        TaskStatus oldStatus = task.getStatus();

        updateTaskFields(task, taskRequest);

        task.setUpdatedAt(Instant.now());

        Task updatedTask = taskRepository.save(task);

        if (oldStatus != updatedTask.getStatus()) {
            outboxEventService.createOutboxEvent(updatedTask, OutboxEventType.TASK_STATUS_CHANGED);
        }

        return taskMapper.toTaskResponse(updatedTask);
    }

    @Transactional
    public void updateTaskStatusById(Long id, TaskStatus taskStatus){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + id));
        task.setStatus(taskStatus);
        taskRepository.save(task);

        outboxEventService.createOutboxEvent(task, OutboxEventType.TASK_STATUS_CHANGED);
    }

    public void deleteTaskById(Long id){
        taskRepository.deleteById(id);
    }

    private void updateTaskFields(Task task, TaskRequest taskRequest) {
        if (taskRequest.title() != null) {
            task.setTitle(taskRequest.title());
        }
        if (taskRequest.titleOnTatar() != null) {
            task.setTitleOnTatar(taskRequest.titleOnTatar());
        }
        if (taskRequest.description() != null) {
            task.setDescription(taskRequest.description());
        }
        if (taskRequest.startAt() != null) {
            task.setStartAt(taskRequest.startAt());
        }
        if (taskRequest.endAt() != null) {
            task.setEndAt(taskRequest.endAt());
        }
        if (taskRequest.expirationAt() != null) {
            task.setExpirationAt(taskRequest.expirationAt());
        }
        if (taskRequest.status() != null) {
            task.setStatus(taskRequest.status());
        }
        if (taskRequest.userId() != null) {
            task.setUserId(taskRequest.userId());
        }
    }
}
