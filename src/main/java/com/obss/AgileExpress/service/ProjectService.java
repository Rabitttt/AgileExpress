package com.obss.AgileExpress.service;


import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.domain.ProjectDao;
import com.obss.AgileExpress.documents.*;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.helper.AuthHelper;
import com.obss.AgileExpress.repository.ElsaticSearch.ProjectESRepository;
import com.obss.AgileExpress.repository.ProjectRepository;
import com.obss.AgileExpress.repository.SprintRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.management.relation.InvalidRoleInfoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final SprintRepository sprintRepository;
    private final ProjectESRepository projectESRepository;
    private final SprintService sprintService;
    private final TaskService taskService;

    private final MongoTemplate mongoTemplate;

    private final AuthHelper authHelper;


    public ProjectService(
            ProjectRepository projectRepository,
            @Lazy UserService userService,
            SprintRepository sprintRepository,
            ProjectESRepository projectESRepository,
            @Lazy SprintService sprintService,
            @Lazy TaskService taskService,
            MongoTemplate mongoTemplate,
            AuthHelper authHelper) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.sprintRepository = sprintRepository;
        this.projectESRepository = projectESRepository;
        this.sprintService = sprintService;
        this.taskService = taskService;
        this.mongoTemplate = mongoTemplate;
        this.authHelper = authHelper;
    }


    public Project createProject(ProjectDao projectDao) {
        //Get Member List
        List<User> members = new ArrayList<>();
        for (String string : projectDao.getMembers()) {
            members.add(userService.getUserById(string));
        }
        //Set basic values
        Sprint sprint = new Sprint();
        sprint.setName("Initial Sprint of " + projectDao.getName());
        sprint.setSprintState("planned");
        sprintRepository.save(sprint);
        //Set Initial Sprint
        List<Sprint> sprints = new ArrayList<>();
        sprints.add(sprint);

        //Set project Object
        List<TaskStatus> taskStatuses = new ArrayList<>(projectDao.getTaskStatus());
        Project project = Project.builder()
                .id(null)
                .name(projectDao.getName())
                .description(projectDao.getDescription())
                .createdDate(null)
                .creator(userService.getUserById(projectDao.getCreator()))
                .projectManager(userService.getUserById(projectDao.getProjectManager()))
                .teamLeader(userService.getUserById(projectDao.getTeamLeader()))
                .members(members)
                .backlogTasks(null)
                .taskStatus(taskStatuses)
                .sprints(sprints)
                .build();

        //Save project to database
        projectRepository.save(project);
        /*
        //Add project to User
        userService.addProjectToUser(project);
         */
        //Save project To ES
        projectESRepository.save(ProjectES.builder().id(project.getId()).name(project.getName()).build());
        log.info("Project created: {}", project);
        return project;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectById(String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        //DELETE sprints
        for (Sprint sprint : project.getSprints()) {
            sprintService.deleteSprint(sprint.getId(),project.getId(),true);
        }
        //DELETE backlog tasks
        project.getBacklogTasks().forEach(item -> taskService.deleteTask(item.getId(),projectId,null));
        /*
        //Delete project from User projects
        userService.deleteProjectFromUser(project);
         */
        //Delete project
        projectRepository.deleteById(projectId);
        //DELETE project from ES
        projectESRepository.deleteById(projectId);

        projectRepository.deleteById(projectId);
        log.info("Project deleted: {}", projectId);
    }


    public Project addTeamMemberToProject(String projectId,String userId) throws Exception {
        try {
            User user = userService.getUserById(userId);
            boolean isRoleTrue = user.getRoles().stream().anyMatch(role-> role.equals(UserRoles.TeamMember.toString()));
            Project project = getProject(projectId, user, isRoleTrue);
            if (project != null) return project;
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Project addTeamLeaderToProject(String projectId,String userId) throws Exception {
        try {
            User user = userService.getUserById(userId);
            boolean isRoleTrue = user.getRoles().stream().anyMatch(role-> role.equals(UserRoles.TeamLeader.name()));
            Project project = getProject(projectId, user, isRoleTrue);
            if (project != null) return project;
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }

    public Project addProjectManagerToProject(String projectId,String userId) throws Exception {
        try {
            User user = userService.getUserById(userId);
            boolean isRoleTrue = user.getRoles().stream().anyMatch(role-> role.equals(UserRoles.ProjectManager.name()));
            Project project = getProject(projectId, user, isRoleTrue);
            if (project != null) return project;
        }
        catch (Exception e) {
            throw e;
        }
        return null;
    }
    private Project getProject(String projectId, User user, boolean isRoleTrue) throws InvalidRoleInfoException {
        if(!isRoleTrue) {
            throw new InvalidRoleInfoException("User is role not a team member");
        }

        Project project = getProjectById(projectId);
        if(project != null) {
            project.getMembers().add(user);
            projectRepository.save(project);
            log.info("User added to project: {}", project);
            return project;
        }
        return null;
    }


    public void removeTeamMember(String projectId,String userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);
        if(project != null) {
            project.getMembers().remove(user);
            projectRepository.save(project);
        }
    }

    public void removeProjectManager(String projectId,String userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);
        if(project != null) {
            project.setProjectManager(null);
            projectRepository.save(project);
        }
    }

    public Project getProjectById(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    public void removeTeamLeader(String projectId,String userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);
        if(project != null) {
            project.setTeamLeader(null);
            projectRepository.save(project);
        }
    }

    public void addBacklogTaskToProject(Task task, String projectId) {
        Project project = getProjectById(projectId);
        project.getBacklogTasks().add(task);
        projectRepository.save(project);
    }
    public void addSprintToProject(Sprint sprint, String projectId) {
        Project project = getProjectById(projectId);
        project.getSprints().add(sprint);
        projectRepository.save(project);
    }

    public void deleteBacklog (Task task,String projectId) {
        Project project = getProjectById(projectId);

        int index = IntStream.range(0, project.getBacklogTasks().size())
                .filter(i -> Objects.equals(task.getId(), project.getBacklogTasks().get(i).getId()))
                .findFirst()
                .orElse(-1);

        project.getBacklogTasks().remove(index);
        projectRepository.save(project);
    }

    public List<User> getTeamMembers(String projectId) {
        Project project = getProjectById(projectId);
        return project.getMembers();
    }

    public void deleteSprintFromProject(String sprintId, String projectId) {
        Project project = getProjectById(projectId);
        int index = IntStream.range(0, project.getSprints().size())
                .filter(i -> Objects.equals(project.getSprints().get(i).getId(), sprintId))
                .findFirst()
                .orElse(-1);
        if(index != -1) {
            project.getSprints().remove(index);
            projectRepository.save(project);
        }
    }

    public void updateProject(String projectId, ProjectDao projectDao) {
        //MONGO UPDATE
        Project project = getProjectById(projectId);
        List<User> updatedProjectMembers = projectDao.getMembers().stream()
                .map(
                userService::getUserByUsername
        ).toList();
        List<User> deletedUsers =
                project.getMembers()
                        .stream()
                        .filter(user -> !updatedProjectMembers.contains(user))
                        .toList();
        deletedUsers.forEach(user -> {
            taskService.findAndDeleteTaskAssignee(user.getUsername());
        });

        project.setName(projectDao.getName());
        project.setDescription(projectDao.getDescription());
        project.setProjectManager(userService.getUserById(projectDao.getProjectManager()));
        project.setProjectManager(userService.getUserById(projectDao.getTeamLeader()));
        project.setMembers(updatedProjectMembers);
        projectRepository.save(project);
        //ES UPDATE
        ProjectES projectES = projectESRepository.findById(projectId).orElse(null);
        projectES.setName(project.getName());
        projectESRepository.save(projectES);
    }

    public List<Project> getAllProjectsByUser() {
        User user = authHelper.getUserPrincipal();
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        criteria.add(Criteria.where("creator").is(user));
        criteria.add(Criteria.where("projectManager").is(user));
        criteria.add(Criteria.where("teamLeader").is(user));
        criteria.add(Criteria.where("members").all(user));

        query.addCriteria(new Criteria().orOperator(criteria.toArray(new Criteria[criteria.size()])));
        List<Project> projects = mongoTemplate.find(query,Project.class);
        return projects;
    }
}
