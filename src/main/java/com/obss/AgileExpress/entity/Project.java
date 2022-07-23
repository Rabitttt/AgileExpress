package com.obss.AgileExpress.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Document("project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    private String id;
    private String name;
    private String description;

    /*
    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;
     */
    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;

    @DocumentReference(lazy = true)
    private User creator;

    @DocumentReference(lazy = true)
    private User teamLeader;

    @DocumentReference(lazy = true)
    private User projectManager;

    @DocumentReference(lazy = true)
    private List<User> members = new ArrayList<>();

    @DocumentReference
    private List<Task> backlogTasks = new ArrayList<>();

    private List<TaskStatus> taskStatus = new ArrayList<>();

    @DocumentReference(lazy = true)
    private List<Sprint> sprints = new ArrayList<>();
}
