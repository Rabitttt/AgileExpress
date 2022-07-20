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

}
