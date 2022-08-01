package com.obss.AgileExpress.documents;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document("task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    private String id;
    private String taskName;
    private String description;
    private int storyPoint;
    private User assignee;
    private String status;
    @DocumentReference(lazy = true)
    private List<TaskLog> taskLogs;
}
