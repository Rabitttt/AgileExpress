package com.obss.AgileExpress.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;


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

    private LocalDateTime expiryDate;

    @DocumentReference(lazy = true)
    private User creator;

    @DocumentReference(lazy = true)
    private User teamLeader;

    @DocumentReference(lazy = true)
    private List<User> members = new ArrayList<>();

    private List<String> tasks = new ArrayList<>();
    private List<String> taskStatus = new ArrayList<>();

}
