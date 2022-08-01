package com.obss.AgileExpress.service;

import com.obss.AgileExpress.entity.Project;
import com.obss.AgileExpress.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private final ProjectRepository projectRepository;
    private final MongoTemplate mongoTemplate;

    public Project getSearchedProject(String searchText) {

        return   projectRepository.searchProjectByName(searchText);


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

    }

}
