package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/task")
@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @GetMapping("/getTask/{taskId}")
    public Task getTask(@PathVariable(value = "taskId") String taskId) {
        return taskService.getTaskById(taskId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/create/{projectId}")
    public Task createBacklogTask(@PathVariable(value = "projectId") String projectId, @RequestBody TaskDao taskDao) {
        return taskService.createTask(taskDao,projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/changeStatus")
    public List<Task> changeTaskStatus(@RequestParam String id, @RequestParam String sprintId, @RequestParam String newStatus) {
        return taskService.changeTaskStatus(id,sprintId,newStatus);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/backlogToSprint")
    public Project backlogToSprint(
            @RequestParam String id,
            @RequestParam String sprintId,
            @RequestParam String newStatus,
            @RequestParam String projectId) {
        return taskService.backlogToSprint(id,sprintId,newStatus,projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/sprintToBacklog")
    public Project backlogToSprint(
            @RequestParam String id,
            @RequestParam String sprintId,
            @RequestParam String projectId) {
        return taskService.sprintToBacklog(id,sprintId,projectId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/update")
    public Task updateTask(
            @RequestBody TaskDao task,
            @RequestParam String taskId) {
        return taskService.updateTask(task,taskId);
    }

}
