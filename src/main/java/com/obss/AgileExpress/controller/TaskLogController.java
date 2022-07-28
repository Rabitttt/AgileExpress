package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.TaskLog;
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
}
