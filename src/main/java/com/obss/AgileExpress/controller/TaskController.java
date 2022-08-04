package com.obss.AgileExpress.controller;

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
    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getTask/{taskId}")
    public Task getTask(@PathVariable(value = "taskId") String taskId) {
        return taskService.getTaskById(taskId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @PostMapping("/create/{projectId}")
    public Task createBacklogTask(@PathVariable(value = "projectId") String projectId, @RequestBody TaskDao taskDao) {
        return taskService.createTask(taskDao,projectId);
    }

    //TODO: everybody can change task status but if role is teamMember he can only change his own task's status
    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/changeStatus")
    public List<Task> changeTaskStatus(@RequestParam String id, @RequestParam String sprintId, @RequestParam String newStatus) {
        return taskService.changeTaskStatus(id,sprintId,newStatus);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/backlogToSprint")
    public Project backlogToSprint(
            @RequestParam String id,
            @RequestParam String sprintId,
            @RequestParam String newStatus,
            @RequestParam String projectId) {
        return taskService.backlogToSprint(id,sprintId,newStatus,projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/sprintToBacklog")
    public Project sprintToBacklog(
            @RequestParam String id,
            @RequestParam String sprintId,
            @RequestParam String projectId) {
        return taskService.sprintToBacklog(id,sprintId,projectId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @PostMapping("/update")
    public Task updateTask(
            @RequestBody TaskDao task,
            @RequestParam String taskId) {
        return taskService.updateTask(task,taskId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')")
    @PostMapping("delete")
    public void deleteTask(
            @RequestParam String taskId,
            @RequestParam String projectId,
            @RequestParam String sprintId
    ) {
        taskService.deleteTask(taskId,projectId,sprintId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/makeMyTask")
    public Task makeMyTask(@RequestParam String taskId) {
        return taskService.makeMyTask(taskId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/dropTaskFromMe")
    public Task dropTaskFromMe(@RequestParam String taskId) {
        return taskService.dropTaskFromMe(taskId);
    }
}
