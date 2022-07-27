package com.obss.AgileExpress.service;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final SprintService sprintService;

    private final UserService userService;

    public Task createTask(TaskDao taskDao) {
        User taskAssignee = userService.getUserById(taskDao.getAssignee());
        Task task = Task.builder()
                .id(null)
                .taskName(taskDao.getTaskName())
                .description(taskDao.getDescription())
                .storyPoint(taskDao.getStoryPoint())
                .assignee(taskAssignee)
                .status("backlog")
                .build();
        Task createdBacklogTask = taskRepository.save(task);
        projectService.addBacklogTaskToProject(createdBacklogTask, "62db079402d1390f0b58f513");
        return createdBacklogTask;
    }

    public List<Task> changeTaskStatus(String id, String sprintId, String newStatus) {
        Task task = taskRepository.findById(id).get();
        task.setStatus(newStatus);
        taskRepository.save(task);
        return sprintService.getSprintTasks(sprintId);
    }

    public Project backlogToSprint(String id, String sprintId, String newStatus, String projectId) {
        Task task = taskRepository.findById(id).get();
        task.setStatus(newStatus);
        taskRepository.save(task);
        projectService.deleteBacklog(task,projectId);
        sprintService.addTaskToSprint(task, sprintId);
        return projectService.getProjectById(projectId);
    }
    public Project sprintToBacklog(String id, String sprintId, String projectId) {
        Task task = taskRepository.findById(id).get();
        task.setStatus("backlog");
        taskRepository.save(task);
        projectService.addBacklogTaskToProject(task,projectId);
        sprintService.deleteTask(task, sprintId);
        return projectService.getProjectById(projectId);
    }

}
