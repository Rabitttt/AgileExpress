package com.obss.AgileExpress.domain;

import com.obss.AgileExpress.entity.TaskLog;
import com.obss.AgileExpress.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private TaskLog taskLog;

}
