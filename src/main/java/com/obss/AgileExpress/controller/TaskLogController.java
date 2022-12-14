package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.service.TaskLogService;
import com.obss.AgileExpress.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/taskLog")
@RestController
@RequiredArgsConstructor
public class TaskLogController {

    private final TaskLogService taskLogService;
    private final TaskService taskService;


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/create/{taskId}")
    public Task createTaskLog(
            @PathVariable(value = "taskId") String taskId,
            @RequestBody TaskLog taskLog,
            @RequestParam String userId
    ) {
        if(!taskService.haveUserAccessToTask(taskId)) {
            return null;
        }
        return taskLogService.createTaskLog(taskId,taskLog,userId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/deleteTaskLog")
    public List<TaskLog> deleteTaskLog(
            @RequestParam String taskId,
            @RequestParam String taskLogId) {
        return taskLogService.deleteTaskLog(taskId,taskLogId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/update")
    public TaskLog updateTaskLog(
            @RequestBody TaskLog taskLog,
            @RequestParam String taskLogId
    ) {
        if(!taskLogService.haveUserAccessToTaskLog(taskLogId)) {
            return null;
        }
        return taskLogService.updateTaskLog(taskLog,taskLogId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getTaskLogsByUserId/{userId}")
    public List<TaskLog> getTasksByUserId(
            @PathVariable(value = "userId") String userId
    ) {
        return taskLogService.getTaskLogsByUserId(userId);
    }
}
