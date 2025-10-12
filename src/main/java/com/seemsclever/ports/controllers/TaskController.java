package com.seemsclever.ports.controllers;

import com.seemsclever.ports.controllers.dto.TaskRequest;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.services.TaskService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest){
        try {
            TaskResponse createdTask = taskService.createTask(taskRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                   @RequestBody TaskRequest taskRequest){
        try{
            TaskResponse updatedTask = taskService.updateTask(id, taskRequest);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

}
