package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
