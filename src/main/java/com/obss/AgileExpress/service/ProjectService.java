package com.obss.AgileExpress.service;


import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project createProject(Project project) {
        projectRepository.save(project);
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
}
