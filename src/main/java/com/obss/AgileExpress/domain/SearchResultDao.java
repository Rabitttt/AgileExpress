package com.obss.AgileExpress.domain;

import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import com.obss.AgileExpress.documents.ElasticSearch.TaskES;
import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import com.obss.AgileExpress.documents.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDao {
    private List<UserES> users;
    private List<ProjectES> projects;
    private List<TaskES> tasks;
}
