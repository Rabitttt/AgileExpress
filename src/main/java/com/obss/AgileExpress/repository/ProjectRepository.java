package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.documents.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project,String> {
    void deleteById(String id);

    @Query("'$search: {autocomplete: { query: \"first\",path: \"name\"}}'")
    Project searchProjectByName(String name);

    @Query("db.AgileExpress.aggregate([{$match: {'project.backlogTasks.ObjectId': $taskId}},{_id: 0, backlogTasks: {$elemMatch: {ObjectId: $taskId}}}])")
    Project getProjectByTaskId(String taskId);

}
