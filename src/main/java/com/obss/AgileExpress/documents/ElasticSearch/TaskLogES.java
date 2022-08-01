package com.obss.AgileExpress.documents.ElasticSearch;

import com.obss.AgileExpress.documents.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLogES {
    @Id
    private String id;
    
    @Field(name = "description",type = FieldType.Text, analyzer = "english")
    private String description;

    @Field(name = "creator",type = FieldType.Text, analyzer = "english")
    private UserES creator;
}
