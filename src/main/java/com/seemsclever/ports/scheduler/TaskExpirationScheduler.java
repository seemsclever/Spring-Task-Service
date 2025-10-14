package com.seemsclever.ports.scheduler;

import com.seemsclever.entities.Task;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.repositories.TaskRepository;
import com.seemsclever.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskExpirationScheduler {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkExpiredTasks(){
        Instant now = Instant.now();

        List<Task> expiredTasks = taskRepository.findExpiredTasks(now, List.of(TaskStatus.COMPLETED, TaskStatus.EXPIRED));

        expiredTasks.forEach(task -> {
            taskService.updateTaskStatusById(task.getId(), TaskStatus.EXPIRED);
        });

    }

}
