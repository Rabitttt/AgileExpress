package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskLogES;
import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.repository.ElsaticSearch.TaskESRepository;
import com.obss.AgileExpress.repository.ElsaticSearch.UserESRepository;
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
    private final TaskESRepository taskESRepository;
    private final UserESRepository userESRepository;

    public Task createTask(TaskDao taskDao,String projectId) {
        User taskAssignee = userService.getUserByUsername(taskDao.getAssignee());
        Task task = Task.builder()
                .id(null)
                .taskName(taskDao.getTaskName())
                .description(taskDao.getDescription())
                .storyPoint(taskDao.getStoryPoint())
                .assignee(taskAssignee)
                .status("backlog")
                .build();
        Task createdBacklogTask = taskRepository.save(task);
        projectService.addBacklogTaskToProject(createdBacklogTask, projectId);
        //Save to ES
        taskESRepository.save(
                TaskES.builder()
                        .id(createdBacklogTask.getId())
                        .taskName(createdBacklogTask.getTaskName())
                        .description(createdBacklogTask.getDescription())
                        .storyPoint(createdBacklogTask.getStoryPoint())
                        //.assignee(userESRepository.findById(createdBacklogTask.getAssignee().getId()).get())
                        .status(createdBacklogTask.getStatus())
                        .build());
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
        projectService.deleteBacklog(task,projectId);
        task.setStatus(newStatus);
        taskRepository.save(task);
        sprintService.addTaskToSprint(task, sprintId);
        return projectService.getProjectById(projectId);
    }
    public Project sprintToBacklog(String id, String sprintId, String projectId) {
        Task task = taskRepository.findById(id).get();
        sprintService.deleteTask(task, sprintId);
        task.setStatus("backlog");
        taskRepository.save(task);
        projectService.addBacklogTaskToProject(task,projectId);
        return projectService.getProjectById(projectId);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id).get();
    }

    public Task  addTaskLogToTask(String taskId, TaskLog taskLog) {
        Task task = taskRepository.findById(taskId).get();
        task.getTaskLogs().add(taskLog);
        taskRepository.save(task);
        //save to ES
        TaskES taskES = taskESRepository.findById(taskId).get();
        taskES.getTaskLogs().add(
                TaskLogES.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .creator(userESRepository.findById(task.getId()).get())
                        .build());
        taskESRepository.save(taskES);
        return task;
    }
}
