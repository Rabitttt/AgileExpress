package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.service.ProjectService;
import com.obss.AgileExpress.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/getAll")
    public List<Project> getAllProjects() {
         return projectService.getAllProjects();
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/create")
    public void createProject(@RequestBody Project project) {
        projectService.createProject(project);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @DeleteMapping("/delete/{projectId}")
    public void deleteProject(@PathVariable(value =  "projectId") String projectId) {
        projectService.deleteProjectById(projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/addTeamMember/{projectId}/{userId}")
    public Project addTeamMember(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) throws Exception {
        return projectService.addTeamMemberToProject(projectId,userId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/addTeamLeader/{projectId}/{userId}")
    public Project addTeamLeader(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) throws Exception {
        return projectService.addTeamLeaderToProject(projectId,userId);
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/addProjectManager/{projectId}/{userId}")
    public Project addProjectManager(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) throws Exception {
        return projectService.addProjectManagerToProject(projectId,userId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @DeleteMapping("/removeTeamMember/{projectId}/{userId}")
    public void removeTeamMember(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) {
        projectService.removeTeamMember(projectId,userId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @DeleteMapping("/removeTeamLeader/{projectId}/{userId}")
    public void removeTeamLeader(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) {
        projectService.removeTeamLeader(projectId,userId);
    }
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/removeProjectManager/{projectId}/{userId}")
    public void removeProjectManager(
            @PathVariable(value = "projectId") String projectId,
            @PathVariable(value = "userId") String userId) {
        projectService.removeProjectManager(projectId,userId);
    }
}
