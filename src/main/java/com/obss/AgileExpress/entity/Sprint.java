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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("sprint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sprint {
    @Id
    private String id;
    private String name;

    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;

    private Date expiryDate;

    private String sprintState;

    @DocumentReference(lazy = true)
    private List<Task> tasks = new ArrayList<>();
}