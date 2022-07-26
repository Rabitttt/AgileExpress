package com.obss.AgileExpress.repository;


import com.obss.AgileExpress.entity.Sprint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends MongoRepository<Sprint,String> {
}
