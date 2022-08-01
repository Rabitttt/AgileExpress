package com.obss.AgileExpress.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private String description;

    private Date startDate;
    private Date endDate;

    private String sprintState;

    @DocumentReference(lazy = true)
    private List<Task> tasks = new ArrayList<>();
}
