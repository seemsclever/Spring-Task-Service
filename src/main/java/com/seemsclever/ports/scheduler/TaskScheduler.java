package com.seemsclever.ports.scheduler;

import com.seemsclever.entities.Task;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.repositories.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@Transactional
public class TaskScheduler {

    private final TaskRepository taskRepository;

    public TaskScheduler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void checkExpiredTasks(){
        List<Task> tasks = taskRepository.findAll();

        Instant now = Instant.now();

        tasks.forEach(task -> {
            if (now.isAfter(task.getExpirationAt()) &&
                    task.getStatus() != TaskStatus.COMPLETED &&
                    task.getStatus() != TaskStatus.EXPIRED){
                task.setStatus(TaskStatus.EXPIRED);
            }
        });

        taskRepository.saveAll(tasks);
    }

}
