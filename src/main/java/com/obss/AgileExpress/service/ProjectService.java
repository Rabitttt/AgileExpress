package com.obss.AgileExpress.service;


import com.obss.AgileExpress.domain.ProjectDao;
import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.entity.Task;
import com.obss.AgileExpress.entity.TaskStatus;
import com.obss.AgileExpress.entity.User;
import com.obss.AgileExpress.enums.UserRoles;
import com.obss.AgileExpress.repository.ProjectRepository;
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
    private final UserService userService;

    //TODO: add tasklist to project
    public Project createProject(ProjectDao projectDao) {
        List<User> members = new ArrayList<>();
        for (String string : projectDao.getMembers()) {
            members.add(userService.getUserById(string));
        }
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
                .build();

        projectRepository.save(project);
        log.info("Project created: {}", project);
        return project;
    }

    public Project addBacklogTaskToProject(Task task,String projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.getBacklogTasks().add(task);
        projectRepository.save(project);
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


}
