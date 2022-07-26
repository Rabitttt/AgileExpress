package com.obss.AgileExpress.service;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.User;
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

    private final UserService userService;

    public Task createTask(TaskDao taskDao) {
        User taskAssignee = userService.getUserById(taskDao.getAssignee());
        Task task = Task.builder()
                .id(null)
                .taskName(taskDao.getTaskName())
                .description(taskDao.getDescription())
                .storyPoint(taskDao.getStoryPoint())
                .assignee(taskAssignee)
                .build();
        Task createdBacklogTask = taskRepository.save(task);
        projectService.addBacklogTaskToProject(createdBacklogTask, "62db079402d1390f0b58f513");
        return createdBacklogTask;
    }
}
