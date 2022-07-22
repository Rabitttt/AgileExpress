package com.obss.AgileExpress.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("taskLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskLog {
    @Id
    private String id;

    private String description;

    @DocumentReference(lazy = true)
    private User creator;


    //TODO: spend time from user
}
