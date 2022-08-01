package com.obss.AgileExpress.service;

import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.Project;
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
        return projectESRepository.findProjectESByNameContains(searchText);
    }
    public List<TaskES> getSearchedTasks(String searchText) {
        return taskESRepository.findTaskESByTaskNameContainsOrDescriptionContainsOrStoryPointContainsOrStatusContainsOrTaskLogsContains(searchText, searchText, searchText, searchText, searchText);
    }
}
















        /*
        Aggregation agg = newAggregation(
                match(where("quantity").gt(1)),
                group("giftCard").count().as("count")
        );

        AggregationResults<Project> results = mongoTemplate.aggregate(
                agg, "order", Project.class
        );
        List<Project> orderCount = results.getMappedResults();
        */
//return null;

