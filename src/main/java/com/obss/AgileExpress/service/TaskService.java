package com.obss.AgileExpress.service;

import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public Task createTask(Task task) {
        Task createdBacklogTask = taskRepository.save(task);
        projectService.addBacklogTaskToProject(createdBacklogTask, "sadf");
        return createdBacklogTask;
    }
}
