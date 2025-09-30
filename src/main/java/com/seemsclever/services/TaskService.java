package com.seemsclever.services;

import com.seemsclever.TaskKafkaProducer;
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

        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found with id " + id));

        updateTaskFields(task, taskRequest);
        task.setUpdatedAt(Instant.now());

        Task updatedTask = taskRepository.save(task);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        taskKafkaProducer.sendTaskToKafka(task);
                    }
                }
        );

        return taskMapper.toTaskResponse(updatedTask);
    }

    private void updateTaskFields(Task task, TaskRequest taskRequest) {
    }

    @Transactional
    public void updateTaskStatusById(Long id, TaskStatus taskStatus){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + id));
        task.setStatus(taskStatus);
        taskRepository.save(task);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        taskKafkaProducer.sendTaskToKafka(task);
                    }
                }
        );
    }

    public void deleteTaskById(Long id){
        taskRepository.deleteById(id);
    }
}
