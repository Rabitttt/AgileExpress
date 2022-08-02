package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.service.TaskLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/taskLog")
@RestController
@RequiredArgsConstructor
public class TaskLogController {

    private final TaskLogService taskLogService;

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @PostMapping("/create/{taskId}")
    public Task createTaskLog(@PathVariable(value = "taskId") String taskId, @RequestBody TaskLog taskLog) {
        return taskLogService.createTaskLog(taskId,taskLog);
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
        return taskLogService.updateTaskLog(taskLog,taskLogId);
    }
}
