package com.obss.AgileExpress.controller;

import com.obss.AgileExpress.documents.User;
import com.obss.AgileExpress.domain.ProjectDao;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.service.ProjectService;
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



    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getAllProjectsByUser")
    public List<Project> getAllProjectsByUser() {
        return projectService.getAllProjectsByUser();
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getProject/{projectId}")
    public Project getProjectById(@PathVariable(value = "projectId") String projectId) {
        return projectService.getProjectById(projectId);
    }
    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getProjectsByUserId/{userId}")
    public List<Project> getProjectsByUserId(
            @PathVariable(value = "userId") String userId
    ) {
        return projectService.getProjectByUserId(userId);
    }


    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/create")
    public void createProject(@RequestBody ProjectDao projectDao) {
        projectService.createProject(projectDao);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')" + "|| hasRole('TeamLeader')" + "|| hasRole('TeamMember')")
    @GetMapping("/getTeamMembers/{projectId}")
    public List<User> getTeamMembers(@PathVariable(value = "projectId") String projectId) {
        return projectService.getTeamMembers(projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/delete")
    public void deleteProject(
            @RequestParam String projectId
    ) {
        projectService.deleteProjectById(projectId);
    }

    @PreAuthorize("hasRole('Admin')" + "|| hasRole('ProjectManager')")
    @PostMapping("/update")
    public void updateProject(
            @RequestParam String projectId,
            @RequestBody ProjectDao projectDao
    ) {
        projectService.updateProject(projectId,projectDao);
    }
}
