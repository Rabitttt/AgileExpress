package com.obss.AgileExpress.domain;

import com.obss.AgileExpress.documents.TaskLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDao {
    private String id;
    private String taskName;
    private String description;
    private int storyPoint;
    private String assignee;
    private String status;
    @DocumentReference(lazy = true)
    private List<TaskLog> taskLogs;

}
