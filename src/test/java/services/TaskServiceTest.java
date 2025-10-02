package services;

import com.seemsclever.entities.Task;
import com.seemsclever.entities.TaskStatus;
import com.seemsclever.mappers.TaskMapper;
import com.seemsclever.ports.controllers.dto.TaskResponse;
import com.seemsclever.repositories.TaskRepository;
import com.seemsclever.services.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;

    @Test
    void testGetAllTasks(){

        Instant now = Instant.now();

        Task task1 = Task.builder()
                .title("Task 1")
                .description("Description 1")
                .createdAt(now)
                .updatedAt(now)
                .startAt(now)
                .endAt(now)
                .expirationAt(now)
                .status(TaskStatus.PENDING)
                .userId(1L)
                .build();

        Task task2 = Task.builder()
                .title("Task 2")
                .description("Description 2")
                .createdAt(now)
                .updatedAt(now)
                .startAt(now)
                .endAt(now)
                .expirationAt(now)
                .status(TaskStatus.IN_PROGRESS)
                .userId(2L)
                .build();

        List<Task> tasks = List.of(task1, task2);

        TaskResponse response1 = new TaskResponse(
                1L, "Task 1", null, "Description 1",
                now, now, now, now, now,
                TaskStatus.PENDING, 1L
        );

        TaskResponse response2 = new TaskResponse(
                2L, "Task 2", null, "Description 2",
                now, now, now, now, now,
                TaskStatus.IN_PROGRESS, 2L
        );

        List<TaskResponse> taskResponses = List.of(response1, response2);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toTaskResponseList(tasks)).thenReturn(taskResponses);

        List<TaskResponse> result = taskService.getAllTasks();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Task 1", result.get(0).title());
        Assertions.assertEquals("Task 2", result.get(1).title());
        Assertions.assertEquals(now, result.get(0).createdAt());
        Assertions.assertEquals(now, result.get(1).createdAt());

        Mockito.verify(taskRepository, times(1)).findAll();
        Mockito.verify(taskMapper, times(1)).toTaskResponseList(tasks);
    }
}
