package com.obss.AgileExpress.repository.ElsaticSearch;

import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskESRepository extends ElasticsearchRepository<TaskES, String> {
    //List<TaskES> findTaskESByTaskNameContainsOrDescriptionContainsOrStoryPointContainsOrAssigneeContainsOrStatusContainsOrTaskLogsContains(String taskName, String description, String storyPoint, String assignee, String status, String taskLogs);
    List<TaskES> findTaskESByTaskNameContainsOrDescriptionContainsOrStoryPointContainsOrStatusContainsOrTaskLogsContains(String taskName, String description, String storyPoint, String status, String taskLogs);
}

