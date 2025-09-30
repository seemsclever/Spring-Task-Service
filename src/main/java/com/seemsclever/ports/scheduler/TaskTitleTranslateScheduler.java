package com.seemsclever.ports.scheduler;

import com.seemsclever.entities.Task;
import com.seemsclever.repositories.TaskRepository;
import com.seemsclever.services.TranslationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TaskTitleTranslateScheduler {

    public final TranslationService translationService;
    public final TaskRepository taskRepository;

    public TaskTitleTranslateScheduler(TranslationService translationService, TaskRepository taskRepository) {
        this.translationService = translationService;
        this.taskRepository = taskRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void translateTitleToTatarLang(){
        List<Task> tasks = taskRepository.findTasksWithEmptyTitleOnTatar();

        tasks.forEach(task ->
                task.setTitleOnTatar(translationService.translateToTatarLang(task.getTitle()))
                );

        taskRepository.saveAll(tasks);
    }
}
