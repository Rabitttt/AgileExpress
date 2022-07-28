package com.obss.AgileExpress.repository;

import com.obss.AgileExpress.entity.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLogRepository extends MongoRepository<TaskLog,String> {

}
