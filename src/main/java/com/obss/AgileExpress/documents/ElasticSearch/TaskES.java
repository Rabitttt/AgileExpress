package com.obss.AgileExpress.documents.ElasticSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(indexName = "taskes")
@Data
@Builder
public class TaskES {
    @Id
    private String id;
    @Field(name = "taskName",type = FieldType.Text, analyzer = "english")
    private String taskName;
    @Field(name = "description",type = FieldType.Text, analyzer = "english")
    private String description;
    @Field(name = "storyPoint",type = FieldType.Text, analyzer = "english")
    private int storyPoint;
    /*
    @Field(name = "assignee",type = FieldType.Nested, includeInParent = true ,analyzer = "english")
    private UserES assignee;
     */
    @Field(name = "status",type = FieldType.Text, analyzer = "english")
    private String status;
    @Field(name = "taskLogs",type = FieldType.Nested, includeInParent = true ,analyzer = "english")
    private List<TaskLogES> taskLogs;
};