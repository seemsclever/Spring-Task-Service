package com.seemsclever.repositories;

import com.seemsclever.entities.Task;
import com.seemsclever.entities.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    @Query(value = """
            SELECT t FROM Task t
                        WHERE t.expirationAt < :now
                                    AND t.status NOT IN :excludedStatuses
            """)
    List<Task> findExpiredTasks(@Param("now") Instant now,
                                @Param("excludedStatuses")List<TaskStatus> excludedStatuses);

    @Query("SELECT t FROM Task t WHERE t.titleOnTatar IS NULL OR t.titleOnTatar = ''")
    List<Task> findTasksWithEmptyTitleOnTatar();
}
