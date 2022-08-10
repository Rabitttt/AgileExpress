package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.repository.TaskLogRepository;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    public Task createTaskLog(String taskId, TaskLog taskLog, String userId) {
        User user = userService.getUserById(userId);
        taskLog.setCreator(user);
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

    public List<TaskLog> getTaskLogsByUserId(String userId) {
        User user = userService.getUserById(userId);
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        criteria.add(Criteria.where("creator").is(user));

        query.addCriteria(new Criteria().orOperator(criteria.toArray(new Criteria[criteria.size()])));
        List<TaskLog> taskLogs = mongoTemplate.find(query,TaskLog.class);
        return taskLogs;
    }

    public Task getTaskByTaskLogId(String taskLogId) {
        TaskLog taskLog = taskLogRepository.findById(taskLogId).get();
        Query query = new Query();

        query.addCriteria(Criteria.where("taskLogs").is(new ObjectId(taskLog.getId())));

        Task task = mongoTemplate.findOne(query,Task.class);
        return task;
    }

    public boolean haveUserAccessToTaskLog(String taskLogId) {
        Task task = getTaskByTaskLogId(taskLogId);
        return taskService.haveUserAccessToTask(task.getId());
    }
}
