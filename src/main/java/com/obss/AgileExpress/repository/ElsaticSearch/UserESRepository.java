package com.obss.AgileExpress.repository.ElsaticSearch;

import com.obss.AgileExpress.documents.ElasticSearch.UserES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserESRepository extends ElasticsearchRepository<UserES, String> {

    List<UserES> findByUsernameContainsOrEmailContainsOrRolesContains(String username,String email,String role);
}
