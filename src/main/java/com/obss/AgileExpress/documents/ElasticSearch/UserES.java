package com.obss.AgileExpress.documents.ElasticSearch;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Collection;

@Document(indexName = "user")
@Data
@Builder
public class UserES {
    @Id
    private String id;
    @Field(name = "username",type = FieldType.Text, analyzer = "english")
    private String username;
    @Field(name = "email",type = FieldType.Text, analyzer = "english")
    private String email;
    private Collection<String> roles = new ArrayList<>();
}
