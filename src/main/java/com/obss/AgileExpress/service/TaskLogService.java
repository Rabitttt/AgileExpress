package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.repository.TaskLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
