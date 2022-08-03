package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.documents.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    public List<Task> findAllByAssignee(String assignee);
}
