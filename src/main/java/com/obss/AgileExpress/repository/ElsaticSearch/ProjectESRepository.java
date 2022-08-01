package com.obss.AgileExpress.repository.ElsaticSearch;

import com.obss.AgileExpress.documents.ElasticSearch.ProjectES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectESRepository extends ElasticsearchRepository<ProjectES, String> {
    List<ProjectES> findProjectESByNameContains(String name);
}
