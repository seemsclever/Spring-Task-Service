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
public class TaskExpirationScheduler {

    private final TaskRepository taskRepository;

    public TaskExpirationScheduler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void checkExpiredTasks(){
        Instant now = Instant.now();

        List<Task> expiredTasks = taskRepository.findExpiredTasks(now, List.of(TaskStatus.COMPLETED, TaskStatus.EXPIRED));

        expiredTasks.forEach(task -> {
            task.setStatus(TaskStatus.EXPIRED);
        });

        taskRepository.saveAll(expiredTasks);
    }

}
