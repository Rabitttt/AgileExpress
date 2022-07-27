package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.entity.Task;
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
    @PostMapping("/create")
    public Task createBacklogTask(@RequestBody TaskDao taskDao) {
        return taskService.createTask(taskDao);
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


}
