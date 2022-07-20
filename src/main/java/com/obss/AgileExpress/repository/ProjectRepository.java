package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project,String> {
    void deleteById(String id);
}
