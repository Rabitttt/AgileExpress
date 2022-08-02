package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.repository.TaskLogRepository;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public Task createTaskLog(String taskId, TaskLog taskLog) {
        TaskLog createdTaskLog = taskLogRepository.save(taskLog);
        return taskService.addTaskLogToTask(taskId,createdTaskLog);
    }

    public List<TaskLog> deleteTaskLog(String taskId,String taskLogId) {
        Task task = taskService.getTaskById(taskId);
        TaskLog taskLog = taskLogRepository.findById(taskLogId).get();

        int index = IntStream.range(0, task.getTaskLogs().size())
                .filter(i -> Objects.equals(task.getTaskLogs().get(i).getId(), taskLogId))
                .findFirst()
                .orElse(-1);

        task.getTaskLogs().remove(index);
        /*
        task.getTaskLogs().stream().findFirst().ifPresent(t -> {
            if (t.getId().equals(taskLogId)) {
                task.getTaskLogs().remove(t);
            }
        });
         */
        taskRepository.save(task);
        taskLogRepository.deleteById(taskLogId);
        return task.getTaskLogs();
    }


    public TaskLog updateTaskLog(TaskLog taskLog,String taskLogId) {
        TaskLog oldTaskLog = taskLogRepository.findById(taskLogId).get();
        oldTaskLog.setDescription(taskLog.getDescription());
        oldTaskLog.setStartClock(taskLog.getStartClock());
        oldTaskLog.setEndClock(taskLog.getEndClock());
        return taskLogRepository.save(oldTaskLog);
    }
}