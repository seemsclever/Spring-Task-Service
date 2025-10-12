package com.seemsclever.metrics;

import com.seemsclever.entities.TaskStatus;
import com.seemsclever.repositories.TaskRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskMetrics {

    public final TaskRepository taskRepository;
    public final MeterRegistry meterRegistry;
    private final Map<TaskStatus, AtomicLong> gauges = new EnumMap<>(TaskStatus.class);

    public TaskMetrics(TaskRepository taskRepository, MeterRegistry meterRegistry) {
        this.taskRepository = taskRepository;
        this.meterRegistry = meterRegistry;

        for (TaskStatus status : TaskStatus.values()) {
            AtomicLong value = new AtomicLong(0);
            meterRegistry.gauge("tasks.by.status", Tags.of("status", status.name()), value);
            gauges.put(status, value);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateTaskMetrics() {
        Map<TaskStatus, Long> counts = new EnumMap<>(TaskStatus.class);

        taskRepository.countTasksGroupedByStatus()
                .forEach(row -> {
                    TaskStatus status = (TaskStatus) row[0];
                    Long count = (Long) row[1];
                    counts.put(status, count);
                });

        counts.forEach((status, count) -> {
            gauges.get(status).set(count);
        });
    }
}
