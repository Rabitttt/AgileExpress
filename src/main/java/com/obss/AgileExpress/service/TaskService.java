package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskLogES;
import com.obss.AgileExpress.domain.TaskDao;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.documents.TaskLog;
import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.helper.AuthHelper;
import com.obss.AgileExpress.repository.ElsaticSearch.TaskESRepository;
import com.obss.AgileExpress.repository.ElsaticSearch.UserESRepository;
import com.obss.AgileExpress.repository.TaskLogRepository;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;
    private final ProjectService projectService;
    @Lazy
    private final SprintService sprintService;
    private final UserService userService;

    private final TaskESRepository taskESRepository;
    private final UserESRepository userESRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthHelper authHelper;

    public Task createTask(TaskDao taskDao,String projectId) {
        User taskAssignee = userService.getUserByUsername(taskDao.getAssignee());
        Task task = Task.builder()
                .id(null)
                .taskName(taskDao.getTaskName())
                .description(taskDao.getDescription())
                .storyPoint(taskDao.getStoryPoint())
                .assignee(taskAssignee)
                .status("backlog")
                .build();
        Task createdBacklogTask = taskRepository.save(task);
        projectService.addBacklogTaskToProject(createdBacklogTask, projectId);
        //Save to ES
        taskESRepository.save(
                TaskES.builder()
                        .id(createdBacklogTask.getId())
                        .taskName(createdBacklogTask.getTaskName())
                        .description(createdBacklogTask.getDescription())
                        .storyPoint(createdBacklogTask.getStoryPoint())
                        //.assignee(userESRepository.findById(createdBacklogTask.getAssignee().getId()).get())
                        .status(createdBacklogTask.getStatus())
                        .build());
        return createdBacklogTask;
    }


    public List<Task> changeTaskStatus(String id, String sprintId, String newStatus) {
        Task task = taskRepository.findById(id).get();

        if(task.getAssignee() != null) {
            User user = authHelper.getUserPrincipal();
            //User role is Team Member , But task assignee is different user. (User only can change own task's status)
            if (!(user.getRoles().toArray()[0].equals(UserRoles.TeamMember.toString()) && !user.getId().equals(task.getAssignee().getId()))) {
                task.setStatus(newStatus);
                taskRepository.save(task);
            }
        }
        return sprintService.getSprintTasks(sprintId);
    }

    public Project backlogToSprint(String id, String sprintId, String newStatus, String projectId) {
        Task task = taskRepository.findById(id).get();
        if(task.getAssignee() != null) {
            User user = authHelper.getUserPrincipal();
            //User role is Team Member , But task assignee is different user. (User only can change own task's status)
            if (!(user.getRoles().toArray()[0].equals(UserRoles.TeamMember.toString()) && !user.getId().equals(task.getAssignee().getId()))) {
                projectService.deleteBacklog(task,projectId);
                task.setStatus(newStatus);
                taskRepository.save(task);
                sprintService.addTaskToSprint(task, sprintId);
            }
        }
        return projectService.getProjectById(projectId);
    }
    public Project sprintToBacklog(String id, String sprintId, String projectId) {
        Task task = taskRepository.findById(id).get();
        if(task.getAssignee() != null) {
            User user = authHelper.getUserPrincipal();
            //User role is Team Member , But task assignee is different user. (User only can change own task's status)
            if (!(user.getRoles().toArray()[0].equals(UserRoles.TeamMember.toString()) && !user.getId().equals(task.getAssignee().getId()))) {
                sprintService.deleteTask(task, sprintId);
                task.setStatus("backlog");
                taskRepository.save(task);
                projectService.addBacklogTaskToProject(task,projectId);

            }
        }
        return projectService.getProjectById(projectId);
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id).get();
    }

    public Task  addTaskLogToTask(String taskId, TaskLog taskLog) {
        Task task = taskRepository.findById(taskId).get();
        task.getTaskLogs().add(taskLog);
        taskRepository.save(task);
        return task;
    }

    public Task updateTask(TaskDao task, String taskId) {
        //UPDATE MONGO
        User user = userService.getUserByUsername(task.getAssignee());
        Task taskToUpdate = taskRepository.findById(taskId).get();
        taskToUpdate.setTaskName(task.getTaskName());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStoryPoint(task.getStoryPoint());
        taskToUpdate.setAssignee(user);
        //UPDATE ELASTİC
        TaskES taskES = taskESRepository.findById(taskId).get();
        taskES.setTaskName(taskToUpdate.getTaskName());
        taskES.setDescription(taskToUpdate.getDescription());
        taskES.setStoryPoint(taskToUpdate.getStoryPoint());
        //UPDATE MONGO AND ELASTIC
        taskRepository.save(taskToUpdate);
        taskESRepository.save(taskES);
        //RETURN TASK
        return taskToUpdate;
    }

    public void deleteTask(String taskId,String projectId,String sprintId) {
        //MONGO
        Task task = taskRepository.findById(taskId).get();
        if(task.getStatus().equals("backlog")) {
            projectService.deleteBacklog(task, projectId);
        }else {
            sprintService.deleteTask(task, sprintId);
        }
        //TASK LOG DELETE
        task.getTaskLogs().forEach(item -> {
            taskLogRepository.deleteById(item.getId());
        });
        //TASK DELETE FROM MONGO
        taskRepository.delete(task);
        //ELASTIC DELETE
        taskESRepository.deleteById(taskId);
    }

    public void findAndDeleteTaskAssignee(String username) {
        User user = userService.getUserByUsername(username);
        Query query = new Query();
        query.addCriteria(Criteria.where("assignee").is(user));
        List<Task> tasks = mongoTemplate.find(query,Task.class);
        tasks.forEach(item -> {
            item.setAssignee(null);
            taskRepository.save(item);
        });
    }

    public Task makeMyTask(String taskId) {
        Task task = getTaskById(taskId);
        User user = authHelper.getUserPrincipal();
        if(task.getAssignee() == null) {
            task.setAssignee(user);
            taskRepository.save(task);
        }
        return task;
    }

    public Task dropTaskFromMe(String taskId) {
        Task task = getTaskById(taskId);
        User user = authHelper.getUserPrincipal();
        if(task.getAssignee().getId().equals(user.getId())) {
            task.setAssignee(null);
            taskRepository.save(task);
        }
        return task;
    }

    public List<Task> userAccessibleTasksForEs() {
        List<Task> accessibleTasks = new ArrayList<>();
        User user = authHelper.getUserPrincipal();

        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        //Accessible Projects from user
        List<Project> accessibleProjects = new ArrayList<>();

        criteria.add(Criteria.where("creator").is(user));
        criteria.add(Criteria.where("projectManager").is(user));
        criteria.add(Criteria.where("teamLeader").is(user));
        criteria.add(Criteria.where("members").all(user));

        query.addCriteria(new Criteria().orOperator(criteria.toArray(new Criteria[criteria.size()])));
        accessibleProjects = mongoTemplate.find(query,Project.class);

        //All accessible Tasks from user accessible Projects
        accessibleProjects.forEach(item -> {
            accessibleTasks.addAll(item.getBacklogTasks());
            item.getSprints().forEach(sprint -> {
                accessibleTasks.addAll(sprint.getTasks());
            });
        });

        return accessibleTasks;
    }

    public List<Task> getTasksByUserId(String userId) {
        User user = userService.getUserById(userId);
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        criteria.add(Criteria.where("assignee").is(user));

        query.addCriteria(new Criteria().orOperator(criteria.toArray(new Criteria[criteria.size()])));
        List<Task> tasks = mongoTemplate.find(query,Task.class);
        return tasks;
    }

    public Task getTaskByTaskLogId(String taskLogId) {
        TaskLog tasklog = taskLogRepository.findById(taskLogId).get();
        Query query = new Query();

        query.addCriteria(Criteria.where("taskLogs").is(new ObjectId(tasklog.getId())));

        Task task = mongoTemplate.findOne(query,Task.class);
        return task;
    }
}
