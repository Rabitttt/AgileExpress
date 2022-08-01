package com.obss.AgileExpress.service;


import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.domain.ProjectDao;
import com.obss.AgileExpress.documents.*;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.repository.ElsaticSearch.ProjectESRepository;
import com.obss.AgileExpress.repository.ProjectRepository;
import com.obss.AgileExpress.repository.SprintRepository;
import com.obss.AgileExpress.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.management.relation.InvalidRoleInfoException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final SprintRepository sprintRepository;
    private final ProjectESRepository projectESRepository;

    public Project createProject(ProjectDao projectDao) {
        List<User> members = new ArrayList<>();
        for (String string : projectDao.getMembers()) {
            members.add(userService.getUserById(string));
        }
        Sprint sprint = new Sprint();
        sprint.setName("Initial Sprint of " + projectDao.getName());
        sprint.setSprintState("planned");
        sprintRepository.save(sprint);
        List<Sprint> sprints = new ArrayList<>();
        sprints.add(sprint);
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

        projectRepository.save(project);
        //Save project To ES
        projectESRepository.save(ProjectES.builder().id(project.getId()).name(project.getName()).build());
        log.info("Project created: {}", project);
        return project;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectById(String projectId) {
        //projectRepository.delete(project);
        projectRepository.deleteById(projectId);
        log.info("Project deleted: {}", projectId);
    }


    public Project addTeamMemberToProject(String projectId,String userId) throws Exception {
        try {
            User user = userService.getUserById(userId);
            /*
            if(user.getRoles().equals("TeamMember")) {
                throw new UsernameNotFoundException("User is role not a team member");
            }
             */
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
            /*
            if(user.getRoles().equals("TeamMember")) {
                throw new UsernameNotFoundException("User is role not a team member");
            }
             */
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
            /*
            if(user.getRoles().equals("TeamMember")) {
                throw new UsernameNotFoundException("User is role not a team member");
            }
             */
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

    //TODO: Check is it requested user's own project
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

        project.getBacklogTasks().stream().findFirst().ifPresent(t->{
            if(t.getId().equals(task.getId())) {
                project.getBacklogTasks().remove(t);
            }
        });
        projectRepository.save(project);
    }

}
