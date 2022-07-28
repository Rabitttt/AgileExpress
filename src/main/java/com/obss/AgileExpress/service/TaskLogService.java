package com.obss.AgileExpress.service;

import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.TaskLog;
import com.obss.AgileExpress.repository.TaskLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskService taskService;

    public Task createTaskLog(String taskId, TaskLog taskLog) {
        TaskLog createdTaskLog = taskLogRepository.save(taskLog);
        return taskService.addTaskLogToTask(taskId,createdTaskLog);
    }
}
