package com.obss.AgileExpress.documents.ElasticSearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "project")
@Data
@Builder
public class ProjectES {
    @Id
    private String id;
    @Field(name = "name",type = FieldType.Text, analyzer = "english")
    private String name;
}