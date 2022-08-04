package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.Project;
import com.obss.AgileExpress.documents.Task;
import com.obss.AgileExpress.domain.SearchResultDao;
import com.obss.AgileExpress.repository.ElsaticSearch.ProjectESRepository;
import com.obss.AgileExpress.repository.ElsaticSearch.TaskESRepository;
import com.obss.AgileExpress.repository.ElsaticSearch.UserESRepository;
import com.obss.AgileExpress.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final UserESRepository userESRepository;
    private final ProjectESRepository projectESRepository;
    private final TaskESRepository taskESRepository;
    private final MongoTemplate mongoTemplate;
    private final ProjectService projectService;
    private final TaskService taskService;

    public SearchResultDao search(String searchText) {

        return SearchResultDao.builder()
                .users(getSearchedUsers(searchText))
                .projects(getSearchedProjects(searchText))
                .tasks(getSearchedTasks(searchText))
                .build();
    }

    public List<UserES> getSearchedUsers(String searchText) {
        return userESRepository.findByUsernameContainsOrEmailContainsOrRolesContains(searchText, searchText,searchText);
    }

    public List<ProjectES> getSearchedProjects(String searchText) {
        List<ProjectES> projectESList = projectESRepository.findProjectESByNameContains(searchText);
        //Is principal user have access to this project?
        List<Project> accessibleProjects = projectService.userAccessibleProjectsForEs(projectESList);
        return projectESList.stream()
                .filter(projectES -> accessibleProjects.stream().anyMatch(project -> project.getId().equals(projectES.getId())))
                .toList();
    }
    public List<TaskES> getSearchedTasks(String searchText) {
        List<TaskES> taskESList = taskESRepository.findTaskESByTaskNameContainsOrDescriptionContainsOrStoryPointContainsOrStatusContainsOrTaskLogsContains(searchText, searchText, searchText, searchText, searchText);
        //Is principal user have access to this tasks?
        List<Task> accessibleTasks = taskService.userAccessibleTasksForEs();
        return taskESList.stream()
                .filter(taskEs -> accessibleTasks.stream().anyMatch(task -> task.getId().equals(taskEs.getId())))
                .toList();
    }
}

